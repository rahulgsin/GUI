/*****************************************************************************
 *                                                                           *
 * FCT - Expert settings dialog                                              *
 *                                                                           *
 * modified: 2012-06-14 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.gui.dialogs;

import de.gsi.sd.common.controls.localizable.LocalizableButton;
import de.gsi.sd.BBQ_Proto1.data.BBQExpertSettings;
import de.gsi.sd.BBQ_Proto1.gui.ExpertSettingsPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ExpertSettingsDialog extends JDialog {

  boolean accepted;
  ExpertSettingsPanel settingsPanel;
  
  public ExpertSettingsDialog(JFrame frame, BBQExpertSettings settings)
  {
    super(frame,"FCT Expert Settings",true);
    initComponents();
    settingsPanel.updateData(settings);
    pack();
    setLocationRelativeTo(frame);
  }
  
  public boolean isAccepted() 
  {
    return accepted;
  }

  public void getSettings(BBQExpertSettings settings)
  {
    settingsPanel.get(settings);
  }


  private void initComponents()
  {
    setLayout(new BorderLayout());
    {
      settingsPanel = new ExpertSettingsPanel(false);
      add(settingsPanel,BorderLayout.CENTER);
    }
    {
      GridLayout layout = new GridLayout(1,1);
      layout.setColumns(1);
      layout.setHgap(5);
      layout.setVgap(5);
      JPanel panel = new JPanel(layout);
      add(panel,BorderLayout.SOUTH);
      {
        JPanel jPanel2 = new JPanel();
        panel.add(jPanel2);
      }
      {
        JButton okButton = new LocalizableButton("Button.Ok");
        panel.add(okButton);
        okButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            accepted = true;
            setVisible(false);
            ((Window)getParent()).toFront();
            getParent().requestFocus();
          }
        });
        getRootPane().setDefaultButton(okButton);
      }
      {
        JButton cancelButton = new LocalizableButton("Button.Cancel");
        panel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            accepted = false;
            setVisible(false);
            ((Window)getParent()).toFront();
            getParent().requestFocus();
          }
        });
      }
    }
  }
  
}
