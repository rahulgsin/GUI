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
 * This class contains the settings (range and offset) of the FESA device. 
 */
public class FESASettingsPath {
	  
	  /** the DataPath value */
	  private String DataPathSetting;

/**
 * Get the range value
 * @return the range
 */
public String getRange() 
{
  return DataPathSetting;
}

/**
 * Set the range value
 * @param raange the range to set
 */
public void setRange(String string) 
{
  this.DataPathSetting = string;
}
}
