/*****************************************************************************
 *                                                                           *
 * BBQ - Simple data statistics                                              *
 *                                                                           *
 * modified: 2010-03-02 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.data;


public class SimpleStatistics {

  public double xMin = 0.0;
  public double xMax = 0.0;
  public int indexMin = 0;
  public int indexMax = 0;
  public double[] sum = new double[BBQConstants.ADC_COUNT];
  public double[] sum2 = new double[BBQConstants.ADC_COUNT];
  public double[] average = new double[BBQConstants.ADC_COUNT];
  public double[] sigma = new double[BBQConstants.ADC_COUNT];
  public long[] counts = new long[BBQConstants.ADC_COUNT];

  public SimpleStatistics()
  {
    for (int i=0;i<BBQConstants.ADC_COUNT;i++)
    {
      sum[i] = 0.0;
      sum2[i] = 0.0;
      average[i] = 0.0;
      sigma[i] = 0.0;
      counts[i] = 0;
    }
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder("Statictics: \n");
    sb.append(String.format("data range : %f - %f\n",xMin,xMax)); 
    sb.append(String.format("index range: %d - %d \n",indexMin,indexMax)); 
    sb.append("sum     ");
    for (int i=0;i<BBQConstants.ADC_COUNT;i++) 
    {
      if (i > 0) sb.append("        ");
      sb.append(String.format("ch %d: %12.7g\n",i,sum[i]));
    }
    sb.append("average ");
    for (int i=0;i<BBQConstants.ADC_COUNT;i++) 
    {
      if (i > 0) sb.append("        ");
      sb.append(String.format("ch %d: %12.7g  sigma %12.7g\n",i,average[i],sigma[i]));
    }
    sb.append("\n");
    return sb.toString();
  }
  
  public void setRange(double min, double max)
  {
    xMin = min;
    xMax = max;
  }
  
  public void setIndexRange(int min, int max)
  {
    indexMin = min;
    indexMax = max;
  }
  
  public void add(int i, double v)
  {
    sum[i] += v;
    sum2[i] += v * v;
    counts[i]++;
  }
  
  public void update()
  {
    for (int i=0;i<BBQConstants.ADC_COUNT;i++)
    {
      if (counts[i] != 0)
      {
        average[i] = sum[i] / counts[i];
        /* calculate the sample standard deviation */
        if (counts[i] > 1)
          sigma[i] = Math.sqrt((counts[i]*sum2[i]-sum[i]*sum[i])/counts[i]/(counts[i]-1));
        else
          sigma[i] = 0.0;
      }
    }
  }
  
}
