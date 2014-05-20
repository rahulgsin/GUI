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
public class FESAAcquisitionSettings {
	  
	  /** ADC mode and settings */
//	  private byte acquisitionmode;
	  private short [] acquisitionsettings;
	  

/**
 * Get the ADC mode
 * @return the ADC mode
 */
	  public FESAAcquisitionSettings()
	  {
		  acquisitionsettings = new short[5];
	  }
	  
/*public byte getMode() 
{
  return acquisitionmode;
}
*/
/**
 * Set the ADC mode
 * @param Set the ADC mode
 */
/*public void setMode(Byte acqmode) 
{
  this.acquisitionmode = acqmode;
}
*/
	  
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
