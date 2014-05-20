/*****************************************************************************
 *                                                                           *
 * FCT - Moving average filter                                               *
 *                                                                           *
 * modified: 2010-12-03 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.data.filter;

import de.gsi.sd.BBQ_Proto1.data.AbstractDataDoubleSet;
import de.gsi.sd.BBQ_Proto1.data.AbstractDataSet;
import de.gsi.sd.BBQ_Proto1.data.FESAData;
import de.gsi.sd.BBQ_Proto1.data.FESAData.FESADataSetContainer;

public class MovingAverageFilter implements DataFilter {

  static public final String ID = "Moving Average Filter"; 
  
  private int length = 3;
  
  public String getName()
  {
    return ID;
  }
  
  @Override
  public boolean execute(FESAData data, DataFilterOptions options) 
  {
    FESADataSetContainer src = data.getDataSet(options.getSource());
    if (src == null) return false;
    FESADataSetContainer dest = data.getDataSet(options.getDestination());
    if (dest == null)
    {
      AbstractDataDoubleSet[] sets = new AbstractDataDoubleSet[src.getDataSets().length];
      for (int i=0;i<sets.length;i++)
      {
        sets[i] = src.getDataSets()[i].clone(options.getDestination());
      }
      dest = new FESADataSetContainer(options.getDestination(),sets);
      data.addDataSet(dest.getName(),dest);
    }
    if (options instanceof MovingAverageFilterOptions)
    {
      length = ((MovingAverageFilterOptions)options).getLength();
    }
    return execute(src,dest);
  }


  private boolean execute(FESADataSetContainer src, FESADataSetContainer dest) 
  {
    boolean flag = true;
    for (int i=0;i<src.getDataSets().length;i++)
    {
      flag &= execute(src.getDataSets()[i],dest.getDataSets()[i]);
    }
    return flag;
  }
  
  private boolean execute(AbstractDataDoubleSet src, AbstractDataDoubleSet dest) 
  {
    double[] srcData = src.getData();
    if (srcData.length <= length) return false;
    double[] destData = new double[srcData.length];
    double acc = 0;
    for (int i=0;i<length;i++) acc += srcData[i];
    destData[length/2] = (double)(acc / length);
    for (int i=length/2+1;i<srcData.length-length/2;i++)
    {
      acc += srcData[i+length/2] - srcData[i-length/2-1];
      destData[i] = acc / length;
    }
    dest.setData(destData);
    return true;
  }

  
  
  
  
  static public class MovingAverageFilterOptions extends DataFilterOptions {

    private int length;
    
    public MovingAverageFilterOptions(String source, String destination)
    {
      super(source, destination);
    }

    /**
     * @return the length
     */
    public int getLength() 
    {
      return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) 
    {
      this.length = length;
    }
    
  }
  
}
