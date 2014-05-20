/*****************************************************************************
 *                                                                           *
 * PrimerExample1 - Settings of FESA class                                   *
 *                                                                           *
 * modified: 2010-08-04 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.data;

/**import de.gsi.sd.BBQ_Proto1.BBQ_GUIApplication;*/

/**
 * This class contains the ADC settings (mode and trigger, acquisition) settings of the FESA device. 
 */
public class FESAAdcSettings {
	  
	  /** ADC mode and settings */
	  private byte adcmode;
	  private short [] adcsettings;
	  

/**
 * Get the ADC mode
 * @return the ADC mode
 */
	  public FESAAdcSettings()
	  {
		  adcsettings = new short[10];
	  }
	  
public byte getMode() 
{
  return adcmode;
}

/**
 * Set the ADC mode
 * @param Set the ADC mode
 */
public void setMode(Byte adcmode) 
{
  this.adcmode = adcmode;
}

public double getAdcSettings(int index) 
{
  return adcsettings[index];
}

public short [] getAdcSettings() 
{
  return adcsettings;
}

public void setAdcSettings(short [] adcsettings) 
{
  this.adcsettings = adcsettings;
}

public void setAdcSettings(short adcsettings,int index) 
{
  this.adcsettings[index] = adcsettings;
}
}
