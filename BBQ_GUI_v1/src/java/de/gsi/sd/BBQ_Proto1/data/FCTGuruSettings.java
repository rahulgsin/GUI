/*****************************************************************************
 *                                                                           *
 * FCT - Guru settings                                                       *
 *                                                                           *
 * modified: 2012-08-28 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.data;

import cern.japc.ValueType;
import de.gsi.sd.common.japc.GenericProperty;

public class FCTGuruSettings extends GenericProperty implements FCTConstants {
  
  static private final String FIELD_TRIGGERTHRESHOLD = "triggerThreshold";
  static private final String FIELD_TRIGGERINPUT = "triggerInput";
  static private final String FIELD_TRIGGEROUTPUT = "triggerOutput";
  static private final String FIELD_TRIGGERINVERT = "invertExternalLemoIn";
  static private final String FIELD_TRIGGERWIDTH = "width";

  
  /**
   * @return the triggerOutputWidth
   */
  public long[] getTriggerOutputWidth() 
  {
    return getLongs(FIELD_TRIGGERWIDTH);
  }

  /**
   * @param triggerOutputWidth the triggerOutputWidth to set
   */
  public void setTriggerOutputWidth(long[] triggerOutputWidth) 
  {
    setField(FIELD_TRIGGERWIDTH,triggerOutputWidth,ValueType.LONG_ARRAY);
  }

  /**
   * @return the triggerInputLemo
   */
  public boolean isTriggerInputLemo() 
  {
    return (getInt(FIELD_TRIGGERINPUT) & 0x0100) != 0;
  }

  /**
   * @param triggerInputLemo the triggerInputLemo to set
   */
  public void setTriggerInputLemo(boolean triggerInputLemo) 
  {
    if (triggerInputLemo)
      setField(FIELD_TRIGGERINPUT,0x0100,ValueType.INT);
    else
      setField(FIELD_TRIGGERINPUT,0x0200,ValueType.INT);
  }

  /**
   * @return the triggerInputNegative
   */
  public boolean isTriggerInputNegative() 
  {
    return getBoolean(FIELD_TRIGGERINVERT);
  }

  /**
   * @param triggerInputNegative the triggerInputNegative to set
   */
  public void setTriggerInputNegative(boolean triggerInputNegative) 
  {
    setField(FIELD_TRIGGERINVERT,triggerInputNegative,ValueType.BOOLEAN);
  }

  /**
   * @return the triggerOutput
   */
  public int getTriggerOutput() 
  {
    int output = 0;
    int flags = getInt(FIELD_TRIGGEROUTPUT);
    if ((flags & 0x0003) != 0) output |= TRIGGER_OUT_LEMO;
    if ((flags & 0x0300) != 0) output |= TRIGGER_OUT_LVDS;
    return output;
  }

  /**
   * @param triggerOutput the triggerOutput to set
   */
  public void setTriggerOutput(int triggerOutput) 
  {
    int flags = 0;
    if ((triggerOutput & TRIGGER_OUT_LEMO) != 0) flags |= 0x0003;
    if ((triggerOutput & TRIGGER_OUT_LVDS) != 0) flags |= 0x0300;
    setField(FIELD_TRIGGEROUTPUT,flags,ValueType.INT);
  }

  /**
   * @return the triggerThreshold
   */
  public double getTriggerThreshold() 
  {
    return getDouble(FIELD_TRIGGERTHRESHOLD);
  }

  /**
   * @param triggerThreshold the triggerThreshold to set
   */
  public void setTriggerThreshold(double triggerThreshold) 
  {
    setField(FIELD_TRIGGERTHRESHOLD,triggerThreshold,ValueType.DOUBLE);
  }

}
