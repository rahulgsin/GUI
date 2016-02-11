/*****************************************************************************
 *                                                                           *
 * BBQ - Guru settings panel                                                 *
 *                                                                           *
 * modified: 2012-06-14 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.gui;

import de.gsi.sd.common.controls.SDPanel;
import de.gsi.sd.common.controls.Updatable;
import de.gsi.sd.common.controls.localizable.LocalizableButton;
import de.gsi.sd.common.controls.localizable.LocalizableLabel;
import de.gsi.sd.BBQ_Proto1.data.BBQExpertSettings;
import de.gsi.sd.BBQ_Proto1.data.BBQGuruSettings;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

@SuppressWarnings("serial")
@Updatable
public class GuruSettingsPanel extends SDPanel {

  private JTextField triggerThreshold;
  private JRadioButton triggerInputLemo;
  private JRadioButton triggerInputLvds;
  private JRadioButton triggerInputPositive;
  private JRadioButton triggerInputNegative;
  private JCheckBox triggerOutputLemo;
  private JCheckBox triggerOutputLvds;
  private JTextField[] widthField = new JTextField[4];

  static private final String[] TRIGGERS = { "Arm", "Trigger", "Stop", "Readout" };

  public GuruSettingsPanel()
  {
    super();
    initComponents(true);
  }

  public GuruSettingsPanel(boolean buttons)
  {
    super();
    initComponents(buttons);
  }

  public void updateData(BBQGuruSettings settings)
  {
    if (settings.isTriggerInputLemo())
      triggerInputLemo.setSelected(true);
    else
      triggerInputLvds.setSelected(true);
    if (settings.isTriggerInputNegative())
      triggerInputNegative.setSelected(true);
    else
      triggerInputPositive.setSelected(true);
    triggerOutputLemo.setSelected((settings.getTriggerOutput() & BBQExpertSettings.TRIGGER_OUT_LEMO) != 0);
    triggerOutputLvds.setSelected((settings.getTriggerOutput() & BBQExpertSettings.TRIGGER_OUT_LVDS) != 0);
    triggerThreshold.setText(String.valueOf(settings.getTriggerThreshold()));
    for (int i=0;i<settings.getTriggerOutputWidth().length;i++)
    {
      widthField[i].setText(String.valueOf(settings.getTriggerOutputWidth()[i]/1000.0));
    }
  }

  @Override
  public void get(Object data)
  {
    if (data instanceof BBQGuruSettings) get((BBQGuruSettings)data);
  }

  private void get(BBQGuruSettings settings)
  {
    settings.setTriggerThreshold(Double.parseDouble(triggerThreshold.getText()));
    settings.setTriggerInputLemo(triggerInputLemo.isSelected());
    settings.setTriggerInputNegative(triggerInputNegative.isSelected());
    int output = 0;
    if (triggerOutputLemo.isSelected()) output |= BBQExpertSettings.TRIGGER_OUT_LEMO;
    if (triggerOutputLvds.isSelected()) output |= BBQExpertSettings.TRIGGER_OUT_LVDS;
    settings.setTriggerOutput(output);
    long[] width = settings.getTriggerOutputWidth();
    for (int i=0;i<4;i++)
    {
      width[i] = (long)(Double.parseDouble(widthField[i].getText()) * 1000.0);
    }
    settings.setTriggerOutputWidth(width);
  }

  private void initComponents(boolean buttons)
  {
    setLayout(new GridBagLayout());
    Insets insets = new Insets(2,2,2,2);
    int topRow = 0;
    {
      JPanel panel = new JPanel(new GridBagLayout());
      add(panel,new GridBagConstraints(1,topRow++,1,1,1.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,insets,0,0));
      panel.setBorder(BorderFactory.createTitledBorder("ADC Trigger"));
      int row = 1;
      {
        LocalizableLabel label = new LocalizableLabel("Label.Threshold");
        panel.add(label,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        triggerThreshold = new JTextField(10);
        panel.add(triggerThreshold,new GridBagConstraints(3,row,2,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        row++;
      }
      {
        LocalizableLabel label = new LocalizableLabel("Label.Input");
        panel.add(label,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        triggerInputLemo = new JRadioButton("Lemo");
        panel.add(triggerInputLemo,new GridBagConstraints(2,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        triggerInputLvds = new JRadioButton("Lvds");
        panel.add(triggerInputLvds,new GridBagConstraints(3,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        ButtonGroup b1 = new ButtonGroup();
        b1.add(triggerInputLemo);
        b1.add(triggerInputLvds);
        row++;
        triggerInputPositive = new JRadioButton("Pos.");
        panel.add(triggerInputPositive,new GridBagConstraints(2,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        triggerInputNegative = new JRadioButton("Neg.");
        panel.add(triggerInputNegative,new GridBagConstraints(3,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        ButtonGroup b2 = new ButtonGroup();
        b2.add(triggerInputNegative);
        b2.add(triggerInputPositive);
        row++;
      }
      {
        LocalizableLabel label = new LocalizableLabel("Label.Output");
        panel.add(label,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        triggerOutputLemo = new JCheckBox("Lemo");
        panel.add(triggerOutputLemo,new GridBagConstraints(2,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        triggerOutputLvds = new JCheckBox("Lvds");
        panel.add(triggerOutputLvds,new GridBagConstraints(3,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        row++;
      }
    }
    {
      JPanel panel = new JPanel(new GridBagLayout());
      add(panel,new GridBagConstraints(1,topRow++,1,1,1.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,insets,0,0));
      panel.setBorder(BorderFactory.createTitledBorder("CTRV Trigger"));
      int row = 1;
      {
        LocalizableLabel label = new LocalizableLabel("Label.CTRWidth");
        panel.add(label,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        for (int i=0;i<TRIGGERS.length;i++)
        {
          panel.add(new JLabel(TRIGGERS[i]),new GridBagConstraints(2,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
          widthField[i] = new JTextField(10);
          panel.add(widthField[i],new GridBagConstraints(3,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
          panel.add(new JLabel("µs"),new GridBagConstraints(4,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
          row++;
        }
      }
    }
/*    
    {
      JPanel hwPanel = new JPanel(new GridBagLayout());
      add(hwPanel,new GridBagConstraints(1,topRow++,1,1,0.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,insets,0,0));
      hwPanel.setBorder(BorderFactory.createTitledBorder("Hardware"));
      int row = 1;
      {
        globalAttenuationLabel = new JLabel(Language.getString("Label.GlobalAttenuation"));
        hwPanel.add(globalAttenuationLabel,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        globalAttenuation = new JSpinner(new SpinnerNumberModel(0,0,40,1));
        hwPanel.add(globalAttenuation,new GridBagConstraints(2,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        row++;
      }
      {
        gainCorrectionLabel = new JLabel(Language.getString("Label.GainCorrection"));
        hwPanel.add(gainCorrectionLabel,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        gainCorrection = new JSpinner(new SpinnerNumberModel(0,-10,10,1));
        hwPanel.add(gainCorrection,new GridBagConstraints(2,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        row++;
      }
    }
*/    
    {
      JPanel fillerPanel = new JPanel(new GridBagLayout());
      add(fillerPanel,new GridBagConstraints(1,topRow++,1,1,0.0,1.0,GridBagConstraints.NORTH,GridBagConstraints.BOTH,insets,0,0));
    }
    if (buttons)
    {
      JPanel buttonPanel = new JPanel(new GridLayout(1,0));
      add(buttonPanel,new GridBagConstraints(1,topRow++,1,1,1.0,0.0,GridBagConstraints.SOUTHWEST,GridBagConstraints.HORIZONTAL,insets,0,0));
      {
        JButton setButton = new LocalizableButton("Button.Set");
        buttonPanel.add(setButton);
        setButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            fireEvent(BBQGUIEvent.ACTION_GURUSETTINGS_SET);
          }
        });
      }
      {
        JButton getButton = new LocalizableButton("Button.Get");
        buttonPanel.add(getButton);
        getButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            fireEvent(BBQGUIEvent.ACTION_GURUSETTINGS_GET);
          }
        });
      }
    }
  }

}
