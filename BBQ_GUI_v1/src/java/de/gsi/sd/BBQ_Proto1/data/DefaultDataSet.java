package de.gsi.sd.BBQ_Proto1.data;

import cern.jdve.utils.DataRange;

public class DefaultDataSet extends AbstractDataDoubleSet {

  public DefaultDataSet(String name) {
    super(name);
    xAxisTitle = "x";
    yAxisTitle = "Data";
  }

  @Override
  public AbstractDataDoubleSet clone(String name)
  {
    DefaultDataSet set = new DefaultDataSet(name);
    set.data = data;
    if (yRange != null) set.yRange = new DataRange(yRange);
    set.yAxisTitle = yAxisTitle;
    set.xAxisTitle = xAxisTitle;
    return set;
  }

  @Override
  public int getIndex(double x) 
  {
    return (int)Math.round(x);
  }

  @Override
  public double getXValue(int index) 
  {
    return index;
  }

}
