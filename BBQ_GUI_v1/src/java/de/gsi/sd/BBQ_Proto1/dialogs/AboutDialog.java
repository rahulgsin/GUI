/*****************************************************************************
 *                                                                           *
 * PrimerExample1 - About dialog                                             *
 *                                                                           *
 * modified: 2010-08-04 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/


package de.gsi.sd.BBQ_Proto1.dialogs;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import cern.accsoft.gui.beans.AboutBox;

/**
 * This about dialog class extends the AboutBox from Cern in order to 
 * set our custom text and a custom icon. Some fields in the about box
 * will be set only, when the application is packaged in a jar file
 * with a manifest. 
 */
@SuppressWarnings("serial")
public class AboutDialog extends AboutBox {

  public AboutDialog(final JFrame frame) {
    super(frame);
    setText("BBQ prototype GUI", "GUI Primer", "(C) Copyright GSI-BD 2010 <br> Harald Braeuning", null);
    try 
    {
      // display the given ImageIcon path
      setIcon(new ImageIcon(this.getClass().getResource("about.jpg")));
    } 
    catch (Exception ex) 
    {
      setIcon(null);
    }
  }

}
