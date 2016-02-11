/*****************************************************************************
 *                                                                           *
 * PrimerExample1 - Settings of FESA class                                   *
 *                                                                           *
 * modified: 2010-08-04 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.data;

import cern.japc.ValueType;

/**import de.gsi.sd.BBQ_Proto1.BBQ_GUIApplication;*/

/**
 * This class contains the ADC settings (mode and trigger, acquisition) settings of the FESA device. 
 */
public class FESAAcquisitionSettings {
	  
	  /** ADC mode and settings */
	  private byte acquisitionmode;
	  private short [] acquisitionsettings;
	  private int acqMode;
	  private double sampleLengthModel;
	  private double preDelay;
	  private long [] delay;
	  private int [] events;
	  
	  

/**
 * Get the ADC mode
 * @return the ADC mode
 */
	  public FESAAcquisitionSettings()
	  {
		  acquisitionsettings = new short[10];
		  delay = new long[2];
		  events = new int[2];
	  }
	  
public byte getMode() 
{
  return acquisitionmode;
}

/**
 * Set the ADC mode
 * @param Set the ADC mode
 */
public void setMode(Byte acqmode) 
{
  this.acquisitionmode = acqmode;
}

public int getAcqMode() 
{
  return acqMode;
}

public void setAcqMode(int acqmode) 
{
  this.acqMode= acqmode;
}

public double getSampleTime() 
{
  return sampleLengthModel;
}

public void setSampleTime(double sampleTime) 
{
  this.sampleLengthModel= sampleTime;
}

public double getPreDelay() 
{
  return preDelay;
}

public void setPreDelay(double predelay) 
{
  this.preDelay= predelay;
}
public long[] getTriggerDelays() 
{
  return delay;
}

public void setTriggerDelays( long [] delay) 
{
  this.delay= delay;
}  

public int[] getTriggerEvents() 
{
  return events;
}

public void setTriggerEvents( int [] event) 
{
  this.events = event;
} 
public double getAcquisitionSettings(int index) 
{
  return acquisitionsettings[index];
}

public short [] getAcquisitionSettings() 
{
  return acquisitionsettings;
}

public void setAcquisitionSettings(short [] acqsettings) 
{
  this.acquisitionsettings = acqsettings;
}

public void setAcquisitionSettings(short acqsettings,int index) 
{
  this.acquisitionsettings[index] = acqsettings;
}

}
