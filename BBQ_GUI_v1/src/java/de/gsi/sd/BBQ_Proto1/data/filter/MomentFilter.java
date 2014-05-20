/*****************************************************************************
 *                                                                           *
 * FCT - Moments filter                                                      *
 *                                                                           *
 * modified: 2010-12-03 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.data.filter;

import de.gsi.sd.BBQ_Proto1.data.AbstractDataDoubleSet;
//import de.gsi.sd.BBQ_Proto1.data.AbstractDataSet;
import de.gsi.sd.BBQ_Proto1.data.DefaultDataSet;
import de.gsi.sd.BBQ_Proto1.data.FESAData;
import de.gsi.sd.BBQ_Proto1.data.FESAData.FESADataSetContainer;

public class MomentFilter implements DataFilter {

  static public final String ID = "Moments Filter"; 

  public String getName()
  {
    return ID;
  }

  @Override
  public boolean execute(FESAData data, DataFilterOptions options) 
  {
    FESADataSetContainer src = data.getDataSet(options.getSource());
    if (src == null) return false;
    String name = options.getDestination();
    AbstractDataDoubleSet[] sets = new DefaultDataSet[4];
    sets[0] = new DefaultDataSet(name+"_Mean");
    FESADataSetContainer c0 =  new FESADataSetContainer(name+"_Mean",sets[0]);
    data.addDataSet(c0.getName(),c0);
    sets[1] = new DefaultDataSet(name+"_Variance");
    FESADataSetContainer c1 =  new FESADataSetContainer(name+"_Variance",sets[1]);
    data.addDataSet(c1.getName(),c1);
    sets[2] = new DefaultDataSet(name+"_Skewness");
    FESADataSetContainer c2 =  new FESADataSetContainer(name+"_Skewness",sets[2]);
    data.addDataSet(c2.getName(),c2);
    sets[3] = new DefaultDataSet(name+"_Kurtosis");
    FESADataSetContainer c3 =  new FESADataSetContainer(name+"_Kurtosis",sets[3]);
    data.addDataSet(c3.getName(),c3);
    return execute(src,sets);
  }


  private boolean execute(FESADataSetContainer src, AbstractDataDoubleSet[] dest) 
  {
    AbstractDataDoubleSet[] sets = src.getDataSets();
    double[] m1 = new double[sets.length];
    double[] m2 = new double[sets.length];
    double[] m3 = new double[sets.length];
    double[] m4 = new double[sets.length];
    double sum;
    for (int i=0;i<sets.length;i++)
    {
      sum = 0;
      m1[i] = 0;
      m2[i] = 0;
      m3[i] = 0;
      m4[i] = 0;
      double[] data = sets[i].getData();
      for (int j=0;j<data.length;j++)
      {
        m1[i] += sets[i].getXValue(j) * data[j];
        sum += data[j];
      }
      m1[i] /= sum;
      for (int j=0;j<data.length;j++)
      {
        double d = (sets[i].getXValue(j) - m1[i]);
        double v = d * d * data[j] / sum;
        m2[i] += v; 
        m3[i] += v * d; 
        m4[i] += v * d * d; 
      }
      m3[i] /= m2[i] * Math.sqrt(m2[i]);
      m4[i] /= m2[i] * m2[i];
    }
    dest[0].setData(m1);
    dest[1].setData(m2);
    dest[2].setData(m3);
    dest[3].setData(m4);
    return true;
  }


}
