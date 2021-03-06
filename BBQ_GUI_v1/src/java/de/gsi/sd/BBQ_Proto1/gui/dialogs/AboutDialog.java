/*****************************************************************************
 *                                                                           *
 * FCT - About dialog                                                        *
 *                                                                           *
 * modified: 2011-01-13 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/


package de.gsi.sd.BBQ_Proto1.gui.dialogs;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import cern.accsoft.gui.beans.AboutBox;


@SuppressWarnings("serial")
public class AboutDialog extends AboutBox {
  
  public AboutDialog(final JFrame frame) {
    super(frame);
    setText("<center>Fast Current Transformer<center>", "FCT_Fesa3", "(C) Copyright GSI-BD 2010-2012 <br>Harald Braeuning <br> Tobias Hoffmann <br> Peter Forck <br> Hannes Reeg", null);
    try 
    {
      // display the given ImageIcon path
      setIcon(new ImageIcon(this.getClass().getResource("about.png")));
    } 
    catch (Exception ex) 
    {
      // show a different image according to the hour of the day
      Calendar cal = new GregorianCalendar();
      int hourOfDay = cal.get(Calendar.HOUR); // 0 => 11...
      String imagePath = "resources/about" + (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ".jpg";
      try 
      {
        // display the given ImageIcon path
        setIcon(new ImageIcon(AboutBox.class.getResource(imagePath)));
      } 
      catch (Exception ex1) 
      {
        try 
        {
          // try to use a default image in case of problem
          setIcon(new ImageIcon(AboutBox.class.getResource("resources/about.jpg")));
        } 
        catch (Exception ex2) 
        {
          // hide ImageIcon in case of problem when getting file path
          setIcon(null);
        }
      }
    }
    pack();
    setLocationRelativeTo(frame);
  }


}
