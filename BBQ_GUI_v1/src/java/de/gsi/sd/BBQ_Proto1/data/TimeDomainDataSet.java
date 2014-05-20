package de.gsi.sd.BBQ_Proto1.data;

import cern.jdve.utils.DataRange;

public class TimeDomainDataSet extends AbstractDataDoubleSet {

  /** Display samples on the x axis */
  static public final int XAXIS_SAMPLES = 0; 
  /** Display seconds on the x axis */
  static public final int XAXIS_SECONDS = 1; 
  /** Display milliseconds on the x axis */
  static public final int XAXIS_MILLISECONDS = 2; 
  /** Display microseconds on the x axis */
  static public final int XAXIS_MICROSECONDS = 3; 

  private int xAxisMode = XAXIS_MILLISECONDS;

  public TimeDomainDataSet(String name) 
  {
    super(name);
    xAxisTitle = "Time [ms]";
  }

  public TimeDomainDataSet(String name, AbstractDataDoubleSet set)
  {
    this(name);
    data = set.data;
    frequency = set.frequency;
    reduction = set.reduction;
    yRange = new DataRange(set.yRange);
    samplingMode = set.samplingMode;
    yAxisTitle = set.yAxisTitle;
  }

  @Override
  public AbstractDataDoubleSet clone(String name)
  {
    return new TimeDomainDataSet(name,this);
  }

  @Override
  public double getXValue(int index) 
  {
    double x = index;
    switch (xAxisMode)
    {
    case XAXIS_SAMPLES:
      return x;
    case XAXIS_SECONDS:
      return x / frequency;
    case XAXIS_MILLISECONDS:
      return x / frequency * 1.0E3;
    case XAXIS_MICROSECONDS:
      return x / frequency * 1.0E6;
    }
    return x;
  }


  @Override
  public int getIndex(double x) {
    switch (xAxisMode)
    {
    case XAXIS_SECONDS:
      x *= frequency;
      break;
    case XAXIS_MILLISECONDS:
      x *= frequency / 1.0E3;
      break;
    case XAXIS_MICROSECONDS:
      x *= frequency / 1.0E6;
      break;
    }
    return (int)x;
  }


}
