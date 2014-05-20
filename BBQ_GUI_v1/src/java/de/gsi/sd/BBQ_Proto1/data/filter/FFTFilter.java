/*****************************************************************************
 *                                                                           *
 * FCT - FFT filter                                                          *
 *                                                                           *
 * modified: 2011-08-15 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.data.filter;

import de.gsi.sd.BBQ_Proto1.data.AbstractDataDoubleSet;
import de.gsi.sd.BBQ_Proto1.data.FESAData;
import de.gsi.sd.BBQ_Proto1.data.FESAData.FESADataSetContainer;
import de.gsi.sd.BBQ_Proto1.data.FrequencyDomainDataSet;
import java.util.Arrays;
import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.transform.FastFourierTransformer;

public class FFTFilter implements DataFilter {

  static public final String ID = "FFT";
  
  private boolean log = false;
  
  @Override
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
      String name = options.getDestination();
      AbstractDataDoubleSet[] sets = new FrequencyDomainDataSet[src.getDataSets().length];
      for (int i=0;i<sets.length;i++)
      {
        sets[i] = new FrequencyDomainDataSet(name,src.getDataSets()[i]);
      }
      dest = new FESADataSetContainer(name,sets);
      data.addDataSet(dest.getName(),dest);
    }
    if (options instanceof FFTFilterOptions)
    {
      log = ((FFTFilterOptions)options).isLogScaling();
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
  
  public boolean execute(AbstractDataDoubleSet src, AbstractDataDoubleSet dest) 
  {
    double[] srcData = pow2Array(src.getData());
    FastFourierTransformer fft = new FastFourierTransformer();
    Complex[] f = fft.transform(srcData);
    double[] destData = new double[f.length/2+1];
    for (int i=0;i<f.length/2+1;i++) 
    {
      destData[i] = (float)f[i].abs();
      if (log) destData[i] = (float)Math.log10(destData[i]);
    }
    dest.setData(destData);
    return false;
  }

  /**
   * Creates a double arrays with a length, which is a power of two from
   * an array of floats
   * @param src
   * @return
   */
  private double[] pow2Array(double[] src)
  {
    int l = 1;
    while (l < src.length) l <<= 1;
    double[] a = new double[l];
    Arrays.fill(a,0);
    for (int i=0;i<src.length;i++) a[i] = src[i];
    return a;
  }
  
  
  
  
  
  static public class FFTFilterOptions extends DataFilterOptions {

    private boolean log;
    
    public FFTFilterOptions(String source, String destination)
    {
      super(source, destination);
    }

    public void setLogScaling(boolean log)
    {
      this.log = log;
    }
    
    public boolean isLogScaling()
    {
      return log;
    }

    
  }
  

}
