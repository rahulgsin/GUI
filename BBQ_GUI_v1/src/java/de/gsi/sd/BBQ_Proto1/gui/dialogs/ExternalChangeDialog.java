/*****************************************************************************
 *                                                                           *
 * FCT - External change of setting notification dialog                      *
 *                                                                           *
 * modified: 2011-10-24 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class ExternalChangeDialog extends JDialog {

  static private ExternalChangeDialog instance = null;
  
  static public ExternalChangeDialog getInstance()
  {
    if (instance == null)
    {
      instance = new ExternalChangeDialog();
    }
    return instance;
  }

  public void show(JFrame frame)
  {
    setLocationRelativeTo(frame);
    setVisible(true);
  }
  
  private ExternalChangeDialog()
  {
    super((Frame)null,"FCT Warning");
    initGUI();
    pack();
  }
  
  private void initGUI()
  {
    setLayout(new BorderLayout());
    {
      JLabel label = new JLabel("FESA class settings have been modified from outside this application");
      label.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
      add(label,BorderLayout.CENTER);
    }
    {
      JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
      add(panel,BorderLayout.SOUTH);
      {
        JButton okButton = new JButton(" Ok ");
        panel.add(okButton);
        okButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            setVisible(false);
            ((Window)getParent()).toFront();
            getParent().requestFocus();
          }
        });
        getRootPane().setDefaultButton(okButton);
      }
    }
  }

}
