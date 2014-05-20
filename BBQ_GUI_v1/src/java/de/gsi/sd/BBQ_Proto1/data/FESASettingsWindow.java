/*****************************************************************************
 *                                                                           *
 * PrimerExample1 - Settings of FESA class                                   *
 *                                                                           *
 * modified: 2010-08-04 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.data;

import de.gsi.sd.BBQ_Proto1.BBQ_GUIApplication;

/**
 * This class contains the settings (range and offset) of the FESA device. 
 */
public class FESASettingsWindow {

  /** the Window value */
  private short DataWindowSetting;

  
  /**
   * Get the offset value
   * @return the offset
   */
  public short getOffset() 
  {
	BBQ_GUIApplication.getLogger().info("Getting settings"); 
    return DataWindowSetting;
  }
  
  /**
   * Set the Window size for FFT
   * @param Window size for FFT
   */
  public void setOffset(short offset) 
  {
    this.DataWindowSetting = offset;
  }
  
}
