/*****************************************************************************
 *                                                                           *
 * PrimerExample1 - Data from FESA class                                     *
 *                                                                           *
 * modified: 2010-08-04 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.data;

/**import de.gsi.sd.BBQ_Proto1.BBQ_GUIApplication;*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import cern.japc.Array2D;
import de.gsi.sd.BBQ_Proto1.BBQ_GUIApplication;
/*import de.gsi.sd.BBQ_Proto1.data.FCTData.FCTDataSetContainer;
import de.gsi.sd.BBQ_Proto1.data.filter.FilterDescriptor;*/
/**
 * This class contains the data obtained from the FESA device by the 
 * JAPCDataProvider
 */
public class FESAData {
	  static public final String CHANNEL_1_TIME = "CH1_Time";
	  static public final String CHANNEL_1_FRE = "CH1_Fre";
	  static public final String CHANNEL_2_TIME = "CH2_Time";
	  static public final String CHANNEL_2_FRE = "CH2_Fre";
	  static public final String CHANNEL_3_TIME = "CH3_Time";
	  static public final String CHANNEL_3_FRE = "CH3_Fre";
	  static public final String CHANNEL_4_TIME = "CH4_Time";
	  static public final String CHANNEL_4_FRE = "CH4_Fre";
	  private int adcValueMode=0;
  /** acquired value */
 // private double[] time;
  private Array2D time;  
  private Array2D spectrum;
  private int samplingFrequency;
  private int num_channels =4;
  private HashMap<String,FESADataSetContainer> dataSetMap = new HashMap<String, FESADataSetContainer>();
  /**
   * Empty constructor
   */
  public FESAData()
  {
	  
	    AbstractDataDoubleSet dataSet1 = new TimeDomainDataSet(CHANNEL_1_TIME);
	    dataSet1.setyAxisTitle(CHANNEL_1_TIME);
	    AbstractDataDoubleSet dataSet2 = new TimeDomainDataSet(CHANNEL_2_TIME);
	    dataSet2.setyAxisTitle(CHANNEL_2_TIME);
	    AbstractDataDoubleSet dataSet3 = new TimeDomainDataSet(CHANNEL_3_TIME);
	    dataSet3.setyAxisTitle(CHANNEL_3_TIME);
	    AbstractDataDoubleSet dataSet4 = new TimeDomainDataSet(CHANNEL_4_TIME);
	    dataSet4.setyAxisTitle(CHANNEL_4_TIME);
	    dataSetMap.put(CHANNEL_1_TIME,new FESADataSetContainer(CHANNEL_1_TIME,dataSet1));
	    dataSetMap.put(CHANNEL_2_TIME,new FESADataSetContainer(CHANNEL_2_TIME,dataSet2));
	    dataSetMap.put(CHANNEL_3_TIME,new FESADataSetContainer(CHANNEL_3_TIME,dataSet3));
	    dataSetMap.put(CHANNEL_4_TIME,new FESADataSetContainer(CHANNEL_4_TIME,dataSet4));
	    dataSet1 = new FrequencyDomainDataSet(CHANNEL_1_FRE);
	    dataSet1.setyAxisTitle(CHANNEL_1_FRE);
	    dataSet2 = new FrequencyDomainDataSet(CHANNEL_2_FRE);
	    dataSet2.setyAxisTitle(CHANNEL_2_FRE);
	    dataSet3 = new FrequencyDomainDataSet(CHANNEL_3_FRE);
	    dataSet3.setyAxisTitle(CHANNEL_3_FRE);
	    dataSet4 = new FrequencyDomainDataSet(CHANNEL_4_FRE);
	    dataSet4.setyAxisTitle(CHANNEL_4_FRE);
	    dataSetMap.put(CHANNEL_1_FRE,new FESADataSetContainer(CHANNEL_1_FRE,dataSet1));
	    dataSetMap.put(CHANNEL_2_FRE,new FESADataSetContainer(CHANNEL_2_FRE,dataSet2));
	    dataSetMap.put(CHANNEL_3_FRE,new FESADataSetContainer(CHANNEL_3_FRE,dataSet3));
	    dataSetMap.put(CHANNEL_4_FRE,new FESADataSetContainer(CHANNEL_4_FRE,dataSet4));
	  /*  dataSet = new TimeDomainDataSet(ADC_CHANNEL_USER1);
	    dataSet.setyAxisTitle(ADC_CHANNEL_USER1);
	    dataSetMap.put(ADC_CHANNEL_USER1,new FCTDataSetContainer(ADC_CHANNEL_USER1,dataSet));
	    dataSet = new TimeDomainDataSet(ADC_CHANNEL_USER2);
	    dataSet.setyAxisTitle(ADC_CHANNEL_USER2);
	    dataSetMap.put(ADC_CHANNEL_USER2,new FCTDataSetContainer(ADC_CHANNEL_USER2,dataSet));*/
  }
  
  /**
   * Constructor for the data value
   * @param value the data value
   */
  public void FESAStoreData(Array2D time, Array2D spectrum)
  {
    this.time = time;
 //   System.out.println("Came to store");
 //   BBQ_GUIApplication.getLogger().info("Came to store");
 //   System.out.println(Arrays.toString(time.getDoubleRow(2)));
 //   this.spectrum = spectrum;
    double[] temp_time = time.getDoubles();
    double[] temp_spectrum = spectrum.getDoubles();
   //System.out.println(Integer.toString(this.channel));
   //System.out.println("from FesaData and channel num");
   System.out.println(temp_time.length);
   //System.out.println(temp_spectrum.length);
    double [] channel_time1 = new double[temp_time.length/num_channels];
    double [] channel_time2 = new double[temp_time.length/num_channels];
    double [] channel_time3 = new double[temp_time.length/num_channels];
    double [] channel_time4 = new double[temp_time.length/num_channels];
    double [] channel_spectrum = new double[temp_spectrum.length/2];
    System.out.println(temp_spectrum.length/2);
    int i=0;
    int j=0;
    do {
        channel_time1[j]=temp_time[i];
        i=i+1;
        channel_time2[j]=temp_time[i];
        i=i+1;
        channel_time3[j]=temp_time[i];
        i=i+1;
        channel_time4[j]=temp_time[i];
        i=i+1;
        j=j+1;
   } while (i < temp_time.length-1);
  //  System.out.println(Arrays.toString(channel_time1));
  //  System.out.println(Arrays.toString(channel_time2));
  //  System.out.println(Arrays.toString(channel_time3));
   // System.out.println(Arrays.toString(channel_time4));   
    i=0;
   // System.out.println(Arrays.toString(channel_time));
  /*  do {
   //     channel_time[j]=temp_time[i];
        channel_spectrum[i]=temp_spectrum[i]*temp_spectrum[i]+temp_spectrum[i+(temp_spectrum.length)/2]*temp_spectrum[i+(temp_spectrum.length)/2];
        i=i+1;
      //  j=j+1;
   } while (i < (temp_spectrum.length)/2);*/
 //   abs_spectrum = spectrum.getDoubleRow(0);
  //  phase_spectrum = spectrum.getDoubleRow(1);
  //  allocateAdcData(time.length);
    setAdcData(channel_time1, 1);
    setAdcData(channel_time2, 2);
    setAdcData(channel_time3, 3);
    setAdcData(channel_time4, 4);
    setAdcFreData(channel_spectrum, 1);
    setAdcFreData(channel_spectrum, 2);
    setAdcFreData(channel_spectrum, 3);
    setAdcFreData(channel_spectrum, 4);
    setSamplingFrequency(100000000);
  }
  
  /**
   * Get the data value
   * @return the value
   */
  public FESAData getCurrentFESAData()
  {
	  return this;
  }
  
  public double[] getTime(int Row) 
  {
    return time.getDoubleRow(Row);
  }

  public double[][] getSpec() 
  {
    return spectrum.getDoubleArray2D();
  }
  
  public void setSamplingFrequency(int frequency) 
  {
	this.samplingFrequency= frequency;
	FESADataSetContainer data1 = dataSetMap.get(CHANNEL_1_TIME);
    data1.getDataSet().setFrequency(frequency);
	FESADataSetContainer data2 = dataSetMap.get(CHANNEL_2_TIME);
    data2.getDataSet().setFrequency(frequency);
	FESADataSetContainer data3 = dataSetMap.get(CHANNEL_3_TIME);
    data3.getDataSet().setFrequency(frequency);
	FESADataSetContainer data4 = dataSetMap.get(CHANNEL_4_TIME);
    data4.getDataSet().setFrequency(frequency);
    data1 = dataSetMap.get(CHANNEL_1_FRE);
    data1.getDataSet().setFrequency(frequency);
  }
  
  public int getSamplingFrequency() 
  {
    return samplingFrequency;
  }
  
  /**
   * Set the data value
   * @param value the value to set
   
  public void setTime(double[] value) 
  {
    this.time = time;
  }*/
 /* public FESAData(Reader reader) throws IOException
  {
    this();
    readAscii(reader);
  }*/
  /**
   * Allocate the memory for the complete ADC data array
   * @param size number of samples for which to allocate memory
   */
 /* public void allocateAdcData(int size) 
  {
    FESADataSetContainer data = dataSetMap.get(CHANNEL_1_TIME);
    data.getDataSet().allocateData(size);
    data = dataSetMap.get(CHANNEL_1_FRE);
    data.getDataSet().allocateData(size);
   /* data = dataSetMap.get(ADC_CHANNEL_USER1);
    data.getDataSet().allocateData(size);
    data = dataSetMap.get(ADC_CHANNEL_USER2);
    data.getDataSet().allocateData(size);
    for (FilterDescriptor fd : filters) executeFilter(fd);*/
 /*   updateAxisTitles();
    System.out.println("Space allocated");
  }*/

  /**
   * Set the complete ADC data array
   * @param time2 the new data array
   */
  public void setAdcData(double[] timeData, int channel) 
  {
	  //FESADataSetContainer data;
	  switch (channel)
      {
     case 1:
    	 FESADataSetContainer data = dataSetMap.get(CHANNEL_1_TIME);
    data.getDataSet().setData(timeData);
    System.out.println(channel); 
    break;
     case 2:
    	 FESADataSetContainer data1 = dataSetMap.get(CHANNEL_2_TIME);
    data1.getDataSet().setData(timeData);
    System.out.println(channel); 
    break;
     case 3:
    	 FESADataSetContainer data2 = dataSetMap.get(CHANNEL_3_TIME);
    data2.getDataSet().setData(timeData);
    System.out.println(channel); 
    break;
     default:
    	 FESADataSetContainer data3 = dataSetMap.get(CHANNEL_4_TIME);
    data3.getDataSet().setData(timeData);
    System.out.println(channel); 
	  }
	
    System.out.println(Arrays.toString(timeData));
  
   /* data = dataSetMap.get(ADC_CHANNEL_USER1);
    data.getDataSet().setData(adcData[2]);
    data = dataSetMap.get(ADC_CHANNEL_USER2);
    data.getDataSet().setData(adcData[3]);
    for (FilterDescriptor fd : filters) executeFilter(fd);*/
    updateAxisTitles();
  }
  public void setAdcFreData(double[] freData, int channel) 
  {
	 // FESADataSetContainer data;
	  switch (channel)
      {
     case 1:
    	 FESADataSetContainer data = dataSetMap.get(CHANNEL_1_FRE);
    data.getDataSet().setData(freData);
    break;
     case 2:
    	 FESADataSetContainer data1 = dataSetMap.get(CHANNEL_2_FRE);
    data1.getDataSet().setData(freData);
    break;
     case 3:
    	 FESADataSetContainer data2 = dataSetMap.get(CHANNEL_3_FRE);
    data2.getDataSet().setData(freData);
    break;
     default:
    	 FESADataSetContainer data3 = dataSetMap.get(CHANNEL_4_FRE);
    data3.getDataSet().setData(freData);
	  }
  
   /* data = dataSetMap.get(ADC_CHANNEL_USER1);
    data.getDataSet().setData(adcData[2]);
    data = dataSetMap.get(ADC_CHANNEL_USER2);
    data.getDataSet().setData(adcData[3]);
    for (FilterDescriptor fd : filters) executeFilter(fd);*/
    updateAxisTitles();
  }
 
  /**
   * Allocate memory for the ADC data array with size data points per channel
   * and copy the given array into it. The size of the data array passed
   * can be smaller or larger then the size argument.
   * @param adcData the new data array to copy
   * @param size number of samples for which to allocate memory
   */
 /* public void setAdcData(float[][] adcData, int size) 
  {
    FESADataSetContainer data = dataSetMap.get(CHANNEL_1_TIME);
    data.getDataSet().setData(adcData[0],size);
    data = dataSetMap.get(CHANNEL_1_FRE);
    data.getDataSet().setData(adcData[1],size);
    data = dataSetMap.get(ADC_CHANNEL_USER1);
    data.getDataSet().setData(adcData[2],size);
    data = dataSetMap.get(ADC_CHANNEL_USER2);
    updateAxisTitles();
  }*/

  /**
   * Copy the given data array into the current data array starting 
   * at the given next sample
   * @param nextSample sample index where to copy the new data array
   * @param adcData new data array
   */
  public void appendAdcData(int nextSample, double[] adcData, double[] freData) {
    FESADataSetContainer data = dataSetMap.get(CHANNEL_1_TIME);
    data.getDataSet().appendData(nextSample,adcData);
    data = dataSetMap.get(CHANNEL_1_FRE);
    data.getDataSet().appendData(nextSample,freData);
    /*data = dataSetMap.get(ADC_CHANNEL_USER1);
    data.getDataSet().appendData(nextSample,adcData[2]);
    data = dataSetMap.get(ADC_CHANNEL_USER2);
    data.getDataSet().appendData(nextSample,adcData[3]);
    if (nextSample+adcData[3].length >= data.getDataSet().getDataCount())
    {
      for (FilterDescriptor fd : filters) executeFilter(fd);
    }*/
  }
  public int getAdcValueMode() {
	    return adcValueMode;
	  }

	  /**
	   * @param adcValueMode the adcValueMode to set
	   */
	  public void setAdcValueMode(int adcValueMode) {
	    this.adcValueMode = adcValueMode;
	  }
  
  private void updateAxisTitles()
  {
    FESADataSetContainer data = dataSetMap.get(CHANNEL_1_TIME);
    switch (adcValueMode)
    {
    case FESAConstants.ADC_VALUEMODE_RAW:
      data.getDataSet().setyAxisTitle(CHANNEL_1_TIME+" [arb.units]");
      break;
    case FESAConstants.ADC_VALUEMODE_VOLTAGE:
      data.getDataSet().setyAxisTitle(CHANNEL_1_TIME+" [V]");
      break;
    }
    data = dataSetMap.get(CHANNEL_1_FRE);
    switch (adcValueMode)
    {
    case FCTConstants.ADC_VALUEMODE_RAW:
      data.getDataSet().setyAxisTitle(CHANNEL_1_FRE+" [arb.units]");
      break;
    case FCTConstants.ADC_VALUEMODE_VOLTAGE:
      data.getDataSet().setyAxisTitle(CHANNEL_1_FRE+" [V]");
      break;
    }
    /*data = dataSetMap.get(ADC_CHANNEL_USER1);
    switch (adcValueMode)
    {
    case FCTConstants.ADC_VALUEMODE_RAW:
      data.getDataSet().setyAxisTitle(ADC_CHANNEL_USER1+" [arb.units]");
      break;
    case FCTConstants.ADC_VALUEMODE_VOLTAGE:
      data.getDataSet().setyAxisTitle(ADC_CHANNEL_USER1+" [V]");
      break;
    }
    data = dataSetMap.get(ADC_CHANNEL_USER2);
    switch (adcValueMode)
    {
    case FCTConstants.ADC_VALUEMODE_RAW:
      data.getDataSet().setyAxisTitle(ADC_CHANNEL_USER2+" [arb.units]");
      break;
    case FCTConstants.ADC_VALUEMODE_VOLTAGE:
      data.getDataSet().setyAxisTitle(ADC_CHANNEL_USER2+" [V]");
      break;
    }*/
  }
  
  public void addDataSet(String name, FESADataSetContainer data)
  {
    dataSetMap.put(name,data);
  }
  
  public String[] getDataSetList()
  {
    Set<String> keys = dataSetMap.keySet();
    String[] list = keys.toArray(new String[keys.size()]);
    Arrays.sort(list);
    return list;
  }

  public FESADataSetContainer getDataSet(String name)
  {
    return dataSetMap.get(name);
  }

  public FESADataSetContainer[] getDataSets(String[] names)
  {
    ArrayList<FESADataSetContainer> list = new ArrayList<FESADataSetContainer>();
    for (String name : names)
    {
      FESADataSetContainer set = dataSetMap.get(name);
      if (set != null) list.add(set);
    }
    return list.toArray(new FESADataSetContainer[list.size()]);
  }
  
/*  
  private void readAscii(Reader reader) throws IOException
  {
    BufferedReader r = new BufferedReader(reader);
    int cols = -1;
    int rows = -1;
    float[][] adcData = null;
    String line = r.readLine();
    while (line != null)
    {
      //      System.out.println(line);
      if (line.startsWith("##"))
      {
        /* Comment line */
      //}
  /*    else if (line.startsWith("#"))
      {
        line = line.substring(1);
        int i = line.indexOf('=');
        if (i > 0)
        {
          String key = line.substring(0,i).trim();
          line = line.substring(i+1);
          String[] values = line.trim().split("\\s+");
//          for (int j=0;j<values.length;j++) System.out.println(j+": "+values[j]);
          if (key.equalsIgnoreCase("ROWS"))
          {
            rows = Integer.parseInt(values[0]);
          }
          else if (key.equalsIgnoreCase("COLUMNS"))
          {
            cols = Integer.parseInt(values[0]);
          }
          else if (key.equalsIgnoreCase("FREQUENCY"))
          {
            setFrequency(Double.parseDouble(values[0]));
          }
          else if (key.equalsIgnoreCase("PRETRIGGER"))
          {
            setPreDelay((int)(Double.parseDouble(values[0])*frequency));
          }
          else if (key.equalsIgnoreCase("MILLIS"))
          {
            setCycleStamp(Long.parseLong(values[0]));
          }
          else if (key.equalsIgnoreCase("CYCLE"))
          {
            setCycleCounter(Long.parseLong(values[0]));
          }
          else if (key.equalsIgnoreCase("VACC"))
          {
            setCycleId(values[0]);
          }
          else if (key.equalsIgnoreCase("TRIGGER_EVENTS"))
          {
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (String value : values)
            {
              value = value.trim();
              if (!value.isEmpty() && Character.isDigit(value.charAt(0))) list.add(Integer.valueOf(value)); 
            }
            events = new int[list.size()];
            for (int index=0;index<list.size();index++) events[index] = list.get(index);
          }
          else if (key.equalsIgnoreCase("TRIGGER_DELAYS"))
          {
            ArrayList<Long> list = new ArrayList<Long>();
            for (String value : values)
            {
              value = value.trim();
              if (!value.isEmpty() && Character.isDigit(value.charAt(0))) list.add(Long.valueOf(value)); 
            }
            delays = new long[list.size()];
            for (int index=0;index<list.size();index++) delays[index] = list.get(index);
          }
          else if (key.equalsIgnoreCase("ADC_RANGE"))
          {
            ArrayList<Double> list = new ArrayList<Double>();
            for (String value : values)
            {
              value = value.trim();
              if (!value.isEmpty() && Character.isDigit(value.charAt(0))) list.add(Double.valueOf(value)); 
            }
            adcRange = new double[list.size()];
            for (int index=0;index<list.size();index++) adcRange[index] = list.get(index);
          }
          else if (key.equalsIgnoreCase("ADC_OFFSET"))
          {
            ArrayList<Short> list = new ArrayList<Short>();
            for (String value : values)
            {
              value = value.trim();
              if (!value.isEmpty() && Character.isDigit(value.charAt(0))) list.add(Short.valueOf(value)); 
            }
            adcOffset = new short[list.size()];
            for (int index=0;index<list.size();index++) adcOffset[index] = list.get(index);
          }
          else if (key.equalsIgnoreCase("HW_GAIN"))
          {
            setGain(Short.parseShort(values[0]));
          }
        }
      }
      else
      {
        if (adcData == null && rows > 0 && cols > 0) adcData = new float[cols][rows];
        if (adcData != null) 
        {
          String[] values = line.trim().split("\\s+");
          if (values.length >= cols)
          {
            int index = Integer.parseInt(values[0]);
            if (index >= 0 && index < rows)
            {
              adcData[0][index] = Float.parseFloat(values[2]);
              adcData[1][index] = Float.parseFloat(values[3]);
              adcData[2][index] = Float.parseFloat(values[4]);
              adcData[3][index] = Float.parseFloat(values[5]);
            }
          }
        }
      }
      line = r.readLine();
    }
    setAdcData(adcData);
  }
*/
  
  static public class FESADataSetContainer {
	    
	    private String name;
	    private AbstractDataDoubleSet[] datasets;
	    
	    public FESADataSetContainer(String name, AbstractDataDoubleSet dataset)
	    {
	      this.name = name;
	      datasets = new AbstractDataDoubleSet[1];
	      datasets[0] = dataset;
	    }
	    
	    public FESADataSetContainer(String name, AbstractDataDoubleSet[] datasets)
	    {
	      this.name = name;
	      this.datasets = datasets;
	    }

	    public String getName()
	    {
	      return name;
	    }
	    
	    public AbstractDataDoubleSet getDataSet()
	    {
	      if (datasets == null || datasets.length == 0) return null;
	      return datasets[0];
	    }
	    
	    public AbstractDataDoubleSet[] getDataSets()
	    {
	      return datasets;
	    }
	    
	  }

public Object[] getFilterList() {
	// TODO Auto-generated method stub
	return null;
}

public String getCycleId() {
	// TODO Auto-generated method stub
	return null;
}

public String getCycleStampString() {
	// TODO Auto-generated method stub
	return null;
}
  
}
