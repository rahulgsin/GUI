package de.gsi.sd.BBQ_Proto1.data;

import cern.jdve.utils.DataRange;

public class TuneDomainDataSet extends AbstractDataDoubleSet {

  public TuneDomainDataSet(String name) 
  {
    super(name);
  //  xAxisTitle = "Frequency [MHz]";
      xAxisTitle = "Fractional tune";
  }

  public TuneDomainDataSet(String name, AbstractDataDoubleSet set)
  {
    this(name);
    data = set.data;
    frequency = set.frequency;
    reduction = set.reduction;
    yRange = new DataRange(set.yRange);
    samplingMode = set.samplingMode;
    yAxisTitle = set.yAxisTitle;
  }

 // @Override
  public AbstractDataDoubleSet clone(String name)
  {
    return new TuneDomainDataSet(name,this);
  }

  @Override
  public double getXValue(int index) 
  {
    return index * frequency / (2.0 * (data.length - 1));
  }


  @Override
  public int getIndex(double x) {
    x *= (data.length - 1) * 2.0 / frequency;
    return (int)x;
  }


}
