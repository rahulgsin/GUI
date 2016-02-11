/*****************************************************************************
 *                                                                           *
 * FCT - Status information                                                  *
 *                                                                           *
 * modified: 2012-06-19 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.data;

import de.gsi.sd.common.japc.StatusProperty;

public class BBQStatus extends StatusProperty implements BBQConstants {

  static private final String CYCLE_COUNTER         = "cycleCounter";
  static private final String LOST_CYCLE_COUNTER    = "lostCycleCounter";
  static private final String IGNORED_CYCLE_COUNTER = "ignoredCycleCounter";
  static private final String TEMPERATURE           = "temperature";
  static private final String FILENAME              = "fileName";
  static private final String FILEOUT_REMAIN        = "fileOutRemain";

  /**
   * @return the temperature
   */
  public double getTemperature() 
  {
    return getDouble(TEMPERATURE);
  }

  /**
   * @return the cycleCounter
   */
  public long getCycleCounter() 
  {
    return getLong(CYCLE_COUNTER);
  }

  /**
   * @return the lostCycleCounter
   */
  public long getLostCycleCounter() 
  {
    return getLong(LOST_CYCLE_COUNTER);
  }

  /**
   * @return the ignoredCycleCounter
   */
  public long getIgnoredCycleCounter() 
  {
    return getLong(IGNORED_CYCLE_COUNTER);
  }

  /**
   * @return the fileName
   */
  public String getFileName() 
  {
    return getString(FILENAME);
  }

  /**
   * @return the fileOutRemain
   */
  public int getFileOutRemain() 
  {
    return getInt(FILEOUT_REMAIN);
  }

  public boolean isNoEvents() 
  {
    return (detailedStatus & NO_EVENTS) != 0;
  }
  
  public boolean isStopped() 
  {
    return (detailedStatus & STOPPED) != 0;
  }
  
  public boolean isWait() 
  {
    return (detailedStatus & WAIT) != 0;
  }
  
  public boolean isAcquire() 
  {
    return (detailedStatus & ACQUIRE) != 0;
  }
  
  public boolean isReadout() 
  {
    return (detailedStatus & READOUT) != 0;
  }
  
  public boolean isOutputInProgress()
  {
    return (detailedStatus & OUTPUT_INPROGRESS) != 0;
  }
  
  public boolean isADCArmed() 
  {
    return (detailedStatus & ADC_ARMED) != 0;
  }
  
  public boolean isADCBusy() 
  {
    return (detailedStatus & ADC_BUSY) != 0;
  }
  
  public boolean isADCEndAddressThreshold() 
  {
    return (detailedStatus & ADC_END_ADDRESS) != 0;
  }
  
  
  
  @Override
  public String getStatusString() 
  {
    if (!connected) return "Disconnected";
    if (isNoEvents()) return "No Events";
    if (isStopped()) return "Stopped";
    return super.getStatusString();
  }
  
  public boolean isError() 
  {
    if (isNoEvents()) return true;
    if (!connected) return true;
    return false;
  }
  
}
