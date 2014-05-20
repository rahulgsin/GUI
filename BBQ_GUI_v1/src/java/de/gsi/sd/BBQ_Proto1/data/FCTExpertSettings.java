/*****************************************************************************
 *                                                                           *
 * FCT - Expert settings                                                     *
 *                                                                           *
 * modified: 2012-08-28 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.data;

import cern.japc.ValueType;
import de.gsi.sd.common.japc.GenericProperty;
import java.util.Arrays;

public class FCTExpertSettings extends GenericProperty implements FCTConstants {
  
  static private final String FIELD_ADCRANGE = "adcRange";
  static private final String FIELD_ADCVALUEMODE = "adcValueMode";
  static private final String FIELD_SAMPLINGFREQUENCY = "frequency";
  static private final String FIELD_FILEMODE = "fileMode";
  static private final String FIELD_FILEPATH = "filePath";
  static private final String FIELD_READBASELINE = "readBaseline";

  @Deprecated
  public FCTExpertSettings clone()
  {
    return null;
  }
  
  @Override
  public boolean equals(Object o)
  {
    if (!(o instanceof FCTExpertSettings)) return false;
    FCTExpertSettings s = (FCTExpertSettings)o;
    if (getFileMode() != s.getFileMode()) return false;
    if (!getFilePath().equals(s.getFilePath())) return false;
    if (getSamplingFrequency() != s.getSamplingFrequency()) return false;
    if (getAdcValueMode() != s.getAdcValueMode()) return false;
    if (isReadBaseline() != s.isReadBaseline()) return false;
    int[] a1 = getAdcRange();
    int[] a2 = s.getAdcRange();
    if (a1.length != a2.length) return false;
    for (int i=0;i<a1.length;i++)
    {
      if (a1[i] != a2[i]) return false;
    }
    return true;
  }
  
  /**
   * @return the readBaseline
   */
  public boolean isReadBaseline() 
  {
    return getBoolean(FIELD_READBASELINE);
  }

  /**
   * @param readBaseline the readBaseline to set
   */
  public void setReadBaseline(boolean readBaseline) 
  {
    setField(FIELD_READBASELINE,readBaseline,ValueType.BOOLEAN);
  }

  /**
   * @return the samplingFrequency
   */
  public int getSamplingFrequency() 
  {
    return getInt(FIELD_SAMPLINGFREQUENCY);
  }

  /**
   * @param samplingFrequency the samplingFrequency to set
   */
  public void setSamplingFrequency(int samplingFrequency) 
  {
    setField(FIELD_SAMPLINGFREQUENCY,samplingFrequency,ValueType.INT);
  }

  /**
   * @return the adcRange
   */
  public int[] getAdcRange() 
  {
    return getInts(FIELD_ADCRANGE);
  }
  
  /**
   * @param adcRange the adcRange to set
   */
  public void setAdcRange(int[] adcRange) 
  {
    setField(FIELD_ADCRANGE,Arrays.copyOf(adcRange,ADC_COUNT),ValueType.INT_ARRAY);
  }
  
  /**
   * @return the adcValueMode
   */
  public int getAdcValueMode() 
  {
    return getInt(FIELD_ADCVALUEMODE);
  }

  /**
   * @param adcValueMode the adcValueMode to set
   */
  public void setAdcValueMode(int adcValueMode) 
  {
    setField(FIELD_ADCVALUEMODE,adcValueMode,ValueType.INT);
  }

  /**
   * @return the fileMode
   */
  public int getFileMode() 
  {
    return getInt(FIELD_FILEMODE);
  }

  /**
   * @param fileMode the fileMode to set
   */
  public void setFileMode(int fileMode) 
  {
    setField(FIELD_FILEMODE,fileMode,ValueType.INT);
  }

  /**
   * @return the filePath
   */
  public String getFilePath() 
  {
    return getString(FIELD_FILEPATH);
  }

  /**
   * @param filePath the filePath to set
   */
  public void setFilePath(String filePath) 
  {
    setField(FIELD_FILEPATH,filePath,ValueType.STRING);
  }

}
