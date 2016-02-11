/*****************************************************************************
 *                                                                           *
 * FCT - Settings                                                            *
 *                                                                           *
 * modified: 2012-08-28 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.data;

import cern.japc.ValueType;
import de.gsi.sd.common.japc.GenericProperty;

public class BBQSettings extends GenericProperty implements BBQConstants {
  
  static private final String FIELD_ACQMODE = "acqMode";
  static private final String FIELD_SAMPLETIME = "sampleTime";
  static private final String FIELD_PREDELAY = "preDelay";
  static private final String FIELD_TRIGGEREVENT = "event";
  static private final String FIELD_TRIGGERDELAY = "delay";
  static private final String FIELD_TRIGGERVACC = "vAcc";
  static private final String FIELD_HWGAIN = "hwGain";
  static private final String FIELD_FILEOUT = "fileOut";

  
  @Deprecated
  public BBQSettings clone()
  {
    return null;
  }
  
  @Override
  public boolean equals(Object o)
  {
    if (!(o instanceof BBQSettings)) return false;
    BBQSettings s = (BBQSettings)o;
    if (isFileOut() != s.isFileOut()) return false;
    if (getAcqMode() != s.getAcqMode()) return false;
    if (getHwGain() != s.getHwGain()) return false;
    if (getSampleTime() != s.getSampleTime()) return false;
    if (getPreDelay() != s.getPreDelay()) return false;
    if (getVAcc() != s.getVAcc()) return false;
    int[] e1 = getTriggerEvents();
    int[] e2 = s.getTriggerEvents();
    if (e1.length != e2.length) return false;
    for (int i=0;i<e1.length;i++)
    {
      if (e1[i] != e2[i]) return false;
    }
    long[] d1 = getTriggerDelays();
    long[] d2 = getTriggerDelays();
    if (d1.length != d2.length) return false;
    for (int i=0;i<d1.length;i++)
    {
      if (d1[i] != d2[i]) return false;
    }
    return true;
  }
  
  /**
   * @return the acqMode
   */
  public int getAcqMode() 
  {
    return getInt(FIELD_ACQMODE);
  }

  /**
   * @param acqMode the acqMode to set
   */
  public void setAcqMode(int acqMode) 
  {
    setField(FIELD_ACQMODE,acqMode,ValueType.INT);
  }

  /**
   * @return the hwGain
   */
  public short getHwGain() 
  {
    return getShort(FIELD_HWGAIN);
  }

  /**
   * @param hwGain the hwGain to set
   */
  public void setHwGain(short hwGain) 
  {
    setField(FIELD_HWGAIN,hwGain,ValueType.SHORT);
  }

  /**
   * @return the sample time in ms
   */
  public double getSampleTime() 
  {
    return getDouble(FIELD_SAMPLETIME);
  }

  /**
   * @param sampleTime the sample time in ms to set
   */
  public void setSampleTime(double sampleTime) 
  {
    setField(FIELD_SAMPLETIME,sampleTime,ValueType.DOUBLE);
  }

  /**
   * @return the preDelay in microseconds
   */
  public double getPreDelay() 
  {
    return getDouble(FIELD_PREDELAY);
  }

  /**
   * @param preDelay the preDelay in microseconds to set
   */
  public void setPreDelay(double preDelay) 
  {
    setField(FIELD_PREDELAY,preDelay,ValueType.DOUBLE);
  }

   /**
   * @return the events
   */
  public int[] getTriggerEvents() 
  {
    return getInts(FIELD_TRIGGEREVENT);
  }

  /**
   * @param events the events to set
   */
  public void setTriggerEvents(int[] events) 
  {
    setField(FIELD_TRIGGEREVENT,events,ValueType.INT_ARRAY);
  }

  /**
   * @return the triggerDelays
   */
  public long[] getTriggerDelays() 
  {
    return getLongs(FIELD_TRIGGERDELAY);
  }

  /**
   * @param triggerDelays the triggerDelays to set
   */
  public void setTriggerDelays(long[] triggerDelays) 
  {
    setField(FIELD_TRIGGERDELAY,triggerDelays,ValueType.LONG_ARRAY);
  }

  /**
   * @return the vAcc
   */
  public short getVAcc() 
  {
    return getShort(FIELD_TRIGGERVACC);
  }

  /**
   * @param acc the vAcc to set
   */
  public void setVAcc(short acc) 
  {
    setField(FIELD_TRIGGERVACC,acc,ValueType.SHORT);
  }

  /**
   * @return the fileOut
   */
  public boolean isFileOut() 
  {
    return getBoolean(FIELD_FILEOUT);
  }

  /**
   * @param fileOut the fileOut to set
   */
  public void setFileOut(boolean fileOut) 
  {
    setField(FIELD_FILEOUT,fileOut,ValueType.BOOLEAN);
  }

  
}
