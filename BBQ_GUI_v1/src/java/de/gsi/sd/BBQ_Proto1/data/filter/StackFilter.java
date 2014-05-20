package de.gsi.sd.BBQ_Proto1.data.filter;

import java.util.Arrays;

import de.gsi.sd.BBQ_Proto1.data.AbstractDataDoubleSet;
import de.gsi.sd.BBQ_Proto1.data.AbstractDataSet;
import de.gsi.sd.BBQ_Proto1.data.FESAData;
import de.gsi.sd.BBQ_Proto1.data.FESAData.FESADataSetContainer;
import de.gsi.sd.BBQ_Proto1.data.TimeDomainDataSet;

public class StackFilter implements DataFilter {

  static public final String ID = "Stack";
  
  private double hfMin;
  private double hfMax;
  private int hfIndex1;
  private int hfIndex2;
  private int hfPeriod;
  private int hfCount;
  private double fctMin;
  private double fctMax;
  private int fctOffset;

  static private final int BUNCH_COUNT = 4;
  
  @Override
  public String getName() 
  {
    return ID;
  }

  @Override
  public boolean execute(FESAData data, DataFilterOptions options) 
  {
    AbstractDataDoubleSet hf = data.getDataSet(FESAData.CHANNEL_1_FRE).getDataSet();
    if (hf == null) return false;
    analyseHF(hf);
    AbstractDataDoubleSet fct = data.getDataSet(FESAData.CHANNEL_1_TIME).getDataSet();
    if (fct == null) return false;
    analyseFCT(fct);
    int n = (hfCount - 1) / BUNCH_COUNT;
    TimeDomainDataSet[][] stack = new TimeDomainDataSet[BUNCH_COUNT][];
    for (int i=0;i<BUNCH_COUNT;i++)
    {
      stack[i] = new TimeDomainDataSet[n];
    }
    stack(fct,hf,stack);
    String destName = options.getDestination();
    if (destName == null || destName.isEmpty()) destName = "Bunch";
    for (int i=0;i<BUNCH_COUNT;i++)
    {
      String name = String.format("%s_%d",destName,i);
      FESADataSetContainer c = new FESADataSetContainer(name,stack[i]);
      data.addDataSet(name,c);
    }
    return true;
  }

  private void analyseHF(AbstractDataDoubleSet hf)
  {
    double[] data = hf.getData();
    hfMin = Double.POSITIVE_INFINITY;
    hfMax = Double.NEGATIVE_INFINITY;
    for (int i=0;i<data.length;i++)
    {
      if (data[i] < hfMin) hfMin = data[i];
      if (data[i] > hfMax) hfMax = data[i];
    }
    double threshold = (hfMax + hfMin) / 2;
    hfIndex1 = -1;
    hfIndex2 = -1;
    hfCount = 0;
    for (int i=0;i<data.length-1;i++)
    {
      if (data[i] > threshold && data[i+1] < threshold)
      {
        if (hfIndex1 < 0)
          hfIndex1 = i;
        else if (hfIndex2 < 0)
          hfIndex2 = i;
        hfCount++;
      }
    }
    hfPeriod = hfIndex2 - hfIndex1;
  }

  private void analyseFCT(AbstractDataDoubleSet fct)
  {
    double[] data = fct.getData();
    fctMin = Double.POSITIVE_INFINITY;
    fctMax = Double.NEGATIVE_INFINITY;
    for (int i=0;i<data.length;i++)
    {
      if (data[i] < fctMin) fctMin = data[i];
      if (data[i] > fctMax) fctMax = data[i];
    }
    double max = fctMin;
    for (int i=hfIndex1;i<hfIndex2;i++)
    {
      if (data[i] > max)
      {
        max = data[i];
        fctOffset = i - hfIndex1;
      }
    }
  }
 
  private void stack(AbstractDataDoubleSet fct, AbstractDataDoubleSet hf, TimeDomainDataSet[][] stack)
  {
    System.out.println("Offset: "+fctOffset+"  Period: "+hfPeriod);
    double[] fctData = fct.getData();
    double[] hfData = hf.getData();
    double threshold = (hfMax + hfMin) / 2;
    int n = 0;
    int m = 0;
    for (int i=0;i<hfData.length-1;i++)
    {
      if (hfData[i] > threshold && hfData[i+1] < threshold)
      {
        int start = i + fctOffset - hfPeriod / 2;
        if (start < 0) continue;
        double[] pulse = Arrays.copyOfRange(fctData,start,start+hfPeriod);
        stack[n][m] = new TimeDomainDataSet(String.format("FCT%d_%d",n,m),fct);
        stack[n][m].setData(pulse);
        n = (n + 1) % BUNCH_COUNT;
        if (n == 0) m++;
        if (m == stack[0].length) break;
      }
    }
  }
  
}
