/*****************************************************************************
 *                                                                           *
 * FCT - Settings panel                                                      *
 *                                                                           *
 * modified: 2012-06-14 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.gui;

import de.gsi.sd.common.controls.SDPanel;
import de.gsi.sd.common.controls.Updatable;
import de.gsi.sd.common.controls.VirtAccSelectorBox;
import de.gsi.sd.common.controls.localizable.LocalizableButton;
import de.gsi.sd.common.controls.localizable.LocalizableCheckBox;
import de.gsi.sd.common.controls.localizable.LocalizableLabel;
import de.gsi.sd.common.controls.localizable.LocalizablePanel;
import de.gsi.sd.common.oper.EventList;
import de.gsi.sd.BBQ_Proto1.data.FCTSettings;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("serial")
@Updatable
public class SettingsPanel extends SDPanel {

  private JComboBox acqMode;
  private EventSelectorBox[] eventSelector = new EventSelectorBox[4];
  private JTextField[] delayField = new JTextField[4];
//  private JRadioButton gain0;
//  private JRadioButton gain20p;
//  private JRadioButton gain20n;
  private VirtAccSelectorBox virtAccSelector;
  private JCheckBox fileOut;
  private SpinnerNumberModel preDelayModel = new SpinnerNumberModel(0,0,320.752,4E-3);
  private SpinnerNumberModel sampleLengthModel = new SpinnerNumberModel(0.001,0,2500,1e-3);
  private SpinnerListModel hwGainModel = new SpinnerListModel(HW_GAIN);
  
  static private final String[] TRIGGERS = { "Arm", "Trigger", "Stop", "Readout" };

  static private final String[] HW_GAIN = { "10dB", "20dB", "30dB", "40dB", "50dB" };
  
  public SettingsPanel()
  {
    super();
    initComponents(true);
  }

  public SettingsPanel(boolean buttons)
  {
    super();
    initComponents(buttons);
  }

  public void updateData(FCTSettings settings)
  {
    acqMode.setSelectedIndex(settings.getAcqMode());
    sampleLengthModel.setValue(settings.getSampleTime());
    preDelayModel.setValue(settings.getPreDelay());
    for (int i=0;i<settings.getTriggerEvents().length;i++)
    {
      int evt = settings.getTriggerEvents()[i];
      eventSelector[i].setSelectedIndex(EventList.getEventIndex(evt));
      delayField[i].setText(String.valueOf((settings.getTriggerDelays()[i]/1000.0)));
    }
//    if (settings.getHwGain() < 0)
//      gain20n.setSelected(true);
//    else if (settings.getHwGain() > 0)
//      gain20p.setSelected(true);
//    else
//      gain0.setSelected(true);
    hwGainModel.setValue(HW_GAIN[settings.getHwGain()]);
    virtAccSelector.setSelectedAccelerator(settings.getVAcc());
    fileOut.setSelected(settings.isFileOut());
  }

  @Override
  public void get(Object data)
  {
    if (data instanceof FCTSettings) get((FCTSettings)data);
    super.get(data);
  }

  private void get(FCTSettings settings)
  {
    settings.setAcqMode(acqMode.getSelectedIndex());
//    settings.setSampleLength(Integer.parseInt(sampleLength.getText()));
    settings.setSampleTime(sampleLengthModel.getNumber().doubleValue());
//    settings.setPreDelay(Integer.parseInt(preDelay.getText()));
    settings.setPreDelay(preDelayModel.getNumber().doubleValue());
    int[] events = new int[4];
    long[] delay = new long[4];
    for (int i=0;i<4;i++)
    {
      int index = eventSelector[i].getSelectedIndex();
      events[i] = EventList.getEventNumbers()[index];
      delay[i] = (long)(Double.parseDouble(delayField[i].getText()) * 1000.0);
    }
    settings.setTriggerDelays(delay);
    settings.setTriggerEvents(events);
//    if (gain20n.isSelected())
//      settings.setHwGain((short)-20);
//    else if (gain20p.isSelected())
//      settings.setHwGain((short)20);
//    else
//      settings.setHwGain((short)0);
    String gain = hwGainModel.getValue().toString();
    for (int i=0;i<HW_GAIN.length;i++)
    {
      if (gain.equals(HW_GAIN[i]))
      {
        settings.setHwGain((short)i);
        break;
      }
    }
    settings.setVAcc(virtAccSelector.getSelectedAccelerator());
    settings.setFileOut(fileOut.isSelected());
  }

  private void initComponents(boolean buttons)
  {
    setLayout(new GridBagLayout());
    Insets insets = new Insets(2,2,2,2);
    int blockRow = 0;
    {
      JPanel panel = new LocalizablePanel(new GridBagLayout());
      add(panel,new GridBagConstraints(1,blockRow,1,1,0.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,insets,0,0));
      panel.setBorder(BorderFactory.createTitledBorder("Title.VirtAcc"));
      int row = 1;
      {
        LocalizableLabel label = new LocalizableLabel("Label.VirtAcc");
        panel.add(label,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        virtAccSelector = new VirtAccSelectorBox();
        panel.add(virtAccSelector,new GridBagConstraints(2,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        row++;
      }
      blockRow++;
    }
    {
      JPanel opPanel = new LocalizablePanel(new GridBagLayout());
      add(opPanel,new GridBagConstraints(1,blockRow,1,1,0.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,insets,0,0));
      opPanel.setBorder(BorderFactory.createTitledBorder("Title.Events"));
      int row = 1;
      opPanel.add(new JLabel("Event"),new GridBagConstraints(2,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
      opPanel.add(new JLabel("Delay / µs"),new GridBagConstraints(3,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
      row++;
      for (int i=0;i<4;i++)
      {
        opPanel.add(new JLabel(TRIGGERS[i]),new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,insets,0,0));
        eventSelector[i] = new EventSelectorBox();
        opPanel.add(eventSelector[i],new GridBagConstraints(2,row,1,1,0.5,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
        delayField[i] = new JTextField(5);
        opPanel.add(delayField[i],new GridBagConstraints(3,row,1,1,0.5,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
        row++;
      }
      blockRow++;
    }
    {
      JPanel opPanel = new LocalizablePanel(new GridBagLayout());
      add(opPanel,new GridBagConstraints(1,blockRow,1,1,0.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,insets,0,0));
      opPanel.setBorder(BorderFactory.createTitledBorder("Title.Acquisition"));
      int row = 1;
      {
        LocalizableLabel label = new LocalizableLabel("Label.AcqMode");
        opPanel.add(label,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        acqMode = new JComboBox(new String[] { "Continuous", "Single shot" });
        opPanel.add(acqMode,new GridBagConstraints(2,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        row++;
      }
      {
        int col = 1;
        LocalizableLabel label = new LocalizableLabel("Label.SampleTime");
        opPanel.add(label,new GridBagConstraints(col++,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
//        sampleLength = new JTextField(10);
        JSpinner sampleLength = new JSpinner(sampleLengthModel);
        sampleLength.setEditor(new JSpinner.NumberEditor(sampleLength,"0.0#####"));
        opPanel.add(sampleLength,new GridBagConstraints(col++,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        opPanel.add(new JLabel("ms"),new GridBagConstraints(col++,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,insets,0,0));
//        row++;
//      }
//      {
//        int col = 1;
        label = new LocalizableLabel("Label.PreDelay");
        opPanel.add(label,new GridBagConstraints(col++,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
//        preDelay = new JTextField(10);
        JSpinner preDelay = new JSpinner(preDelayModel);
        preDelay.setEditor(new JSpinner.NumberEditor(preDelay,"0.0##"));
        opPanel.add(preDelay,new GridBagConstraints(col++,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        opPanel.add(new JLabel("µs"),new GridBagConstraints(col++,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,insets,0,0));
        row++;
      }
      blockRow++;
    }
    {
      JPanel hwPanel = new LocalizablePanel(new GridBagLayout());
      add(hwPanel,new GridBagConstraints(1,blockRow,1,1,0.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,insets,0,0));
      hwPanel.setBorder(BorderFactory.createTitledBorder("Title.Hardware"));
      int row = 1;
      {
        int col = 1;
        LocalizableLabel label = new LocalizableLabel("Label.Gain");
        hwPanel.add(label,new GridBagConstraints(col++,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        JSpinner spinner = new JSpinner(hwGainModel);
        hwPanel.add(spinner,new GridBagConstraints(col++,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
//        gain20n = new JRadioButton("-20db");
//        hwPanel.add(gain20n,new GridBagConstraints(col++,row,1,1,1.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
////        row++;
//        gain0 = new JRadioButton("  0db");
//        hwPanel.add(gain0,new GridBagConstraints(col++,row,1,1,1.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
////        row++;
//        gain20p = new JRadioButton("+20db");
//        hwPanel.add(gain20p,new GridBagConstraints(col++,row,1,1,1.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
////        row++;
//        ButtonGroup b1 = new ButtonGroup();
//        b1.add(gain20n);
//        b1.add(gain0);
//        b1.add(gain20p);
      }
      blockRow++;
    }
    {
      JPanel filePanel = new LocalizablePanel(new GridBagLayout());
      add(filePanel,new GridBagConstraints(1,blockRow,1,1,0.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,insets,0,0));
      filePanel.setBorder(BorderFactory.createTitledBorder("Title.Output"));
      int row = 1;
      {
//        JLabel label = new JLabel(Language.getString("Label.FileMode"));
//        filePanel.add(label,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        fileOut = new LocalizableCheckBox("Label.FileOut");
        filePanel.add(fileOut,new GridBagConstraints(1,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        row++;
      }
      blockRow++;
    }
    {
      JPanel fillerPanel = new JPanel(new GridBagLayout());
      add(fillerPanel,new GridBagConstraints(1,blockRow,1,1,0.0,1.0,GridBagConstraints.NORTH,GridBagConstraints.BOTH,insets,0,0));
      blockRow++;
    }
    if (buttons)
    {
      JPanel buttonPanel = new JPanel(new GridLayout(1,0));
      add(buttonPanel,new GridBagConstraints(1,blockRow,1,1,1.0,0.0,GridBagConstraints.SOUTHWEST,GridBagConstraints.HORIZONTAL,insets,0,0));
      {
        LocalizableButton setButton = new LocalizableButton("Button.Set");
        buttonPanel.add(setButton);
        setButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            fireEvent(FCTGUIEvent.ACTION_SETTINGS_SET);
          }
        });
      }
      {
        LocalizableButton getButton = new LocalizableButton("Button.Get");
        buttonPanel.add(getButton);
        getButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            fireEvent(FCTGUIEvent.ACTION_SETTINGS_GET);
          }
        });
      }
      blockRow++;
    }
  }

}
