/*****************************************************************************
 *                                                                           *
 * FCT - Expert settings panel                                               *
 *                                                                           *
 * modified: 2012-06-14 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.gui;

import de.gsi.sd.common.controls.SDPanel;
import de.gsi.sd.common.controls.Updatable;
import de.gsi.sd.common.controls.localizable.LocalizableButton;
import de.gsi.sd.common.controls.localizable.LocalizableLabel;
import de.gsi.sd.common.controls.localizable.LocalizablePanel;
import de.gsi.sd.BBQ_Proto1.data.FCTConstants;
import de.gsi.sd.BBQ_Proto1.data.FCTExpertSettings;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
@Updatable
public class ExpertSettingsPanel extends SDPanel {

  private JComboBox samplingFrequency;
//  private JLabel[] adcLabel = new JLabel[FCTExpertSettings.ADC_COUNT];
  private JComboBox[] adcRange = new JComboBox[FCTExpertSettings.ADC_COUNT];
  private JComboBox adcValueMode;
  private JTextField filePath;
  private JComboBox fileMode;
  /* TODO: make readBaseLine setting changable */
  /* stores readBaseLine setting which is currently not settable in the GUI */
  private boolean readBaseline;

  public ExpertSettingsPanel()
  {
    super();
    initComponents(true);
  }

  public ExpertSettingsPanel(boolean buttons)
  {
    super();
    initComponents(buttons);
  }

  public void updateData(FCTExpertSettings settings)
  {
    readBaseline = settings.isReadBaseline();
//    samplingFrequency.setText(String.valueOf(settings.getSamplingFrequency()));
    if (settings.getSamplingFrequency() > 375)
    {
      samplingFrequency.setSelectedItem(FCTConstants.FREQUENCY[3]);
    }
    else if (settings.getSamplingFrequency() > 175)
    {
      samplingFrequency.setSelectedItem(FCTConstants.FREQUENCY[2]);
    }
    else if (settings.getSamplingFrequency() > 75)
    {
      samplingFrequency.setSelectedItem(FCTConstants.FREQUENCY[1]);
    }
    else
    {
      samplingFrequency.setSelectedItem(FCTConstants.FREQUENCY[0]);
    }
    for (int i=0;i<settings.getAdcRange().length;i++)
    {
//      adcRange[i].setSelectedIndex(getADCRange(settings.getAdcRange()[i]));
      adcRange[i].setSelectedIndex(settings.getAdcRange()[i]);
      adcValueMode.setSelectedIndex(settings.getAdcValueMode());
    }
    filePath.setText(settings.getFilePath());
    fileMode.setSelectedIndex(settings.getFileMode());
  }

  @Override
  public void get(Object data)
  {
    if (data instanceof FCTExpertSettings) get((FCTExpertSettings)data);
  }

  private void get(FCTExpertSettings settings)
  {
    settings.setReadBaseline(readBaseline);
    switch (samplingFrequency.getSelectedIndex())
    {
    case 0:
      settings.setSamplingFrequency(50);
      break;
    case 1:
      settings.setSamplingFrequency(100);
      break;
    case 2:
      settings.setSamplingFrequency(250);
      break;
    case 3:
      settings.setSamplingFrequency(500);
      break;
    }
    int[] range = new int[FCTExpertSettings.ADC_COUNT];
    for (int i=0;i<FCTExpertSettings.ADC_COUNT;i++)
    {
      range[i] = adcRange[i].getSelectedIndex();
    }
    settings.setAdcRange(range);
    settings.setAdcValueMode(adcValueMode.getSelectedIndex());
    settings.setFileMode(fileMode.getSelectedIndex());
    settings.setFilePath(filePath.getText());
  }

  private void initComponents(boolean buttons)
  {
    setLayout(new GridBagLayout());
    Insets insets = new Insets(2,2,2,2);
    {
      JPanel adcPanel = new LocalizablePanel(new GridBagLayout());
      add(adcPanel,new GridBagConstraints(1,2,1,1,1.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,insets,0,0));
      adcPanel.setBorder(BorderFactory.createTitledBorder("Title.ADC"));
      int row = 1;
      {
        LocalizableLabel label = new LocalizableLabel("Label.Range");
        adcPanel.add(label,new GridBagConstraints(2,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
        row++;
      }
      for (int i=0;i<FCTConstants.ADC_COUNT;i++)
      {
//        adcLabel[i] = new LocalizableLabel(language,FCTConstants.ADCLABELS[i]);
//        adcPanel.add(adcLabel[i],new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
        LocalizableLabel label = new LocalizableLabel(FCTConstants.ADCLABELS[i]);
        adcPanel.add(label,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
        adcRange[i] = new JComboBox(FCTConstants.ADCRANGE);
        adcPanel.add(adcRange[i],new GridBagConstraints(2,row,1,1,1.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
        row++;
      }
      {
        LocalizableLabel label = new LocalizableLabel("Label.ValueMode");
        adcPanel.add(label,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
        adcValueMode = new JComboBox(FCTConstants.ADCVALUEMODE);
        adcPanel.add(adcValueMode,new GridBagConstraints(2,row,1,1,1.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
        row++;
      }
      {
        LocalizableLabel label = new LocalizableLabel("Label.Frequency");
        adcPanel.add(label,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
        samplingFrequency = new JComboBox(FCTConstants.FREQUENCY);
        adcPanel.add(samplingFrequency,new GridBagConstraints(2,row,1,1,1.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
        row++;
      }
    }
    {
      JPanel filePanel = new LocalizablePanel(new GridBagLayout());
      add(filePanel,new GridBagConstraints(1,5,1,1,0.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,insets,0,0));
      filePanel.setBorder(BorderFactory.createTitledBorder("Title.Output"));
      int row = 1;
      {
        LocalizableLabel label = new LocalizableLabel("Label.FileMode");
        filePanel.add(label,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        fileMode = new JComboBox(FCTConstants.FILEMODE);
        filePanel.add(fileMode,new GridBagConstraints(2,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        row++;
      }
      {
        LocalizableLabel label = new LocalizableLabel("Label.FilePath");
        filePanel.add(label,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        filePath = new JTextField(25);
        filePanel.add(filePath,new GridBagConstraints(2,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        row++;
      }
    }
    {
      JPanel fillerPanel = new JPanel(new GridBagLayout());
      add(fillerPanel,new GridBagConstraints(1,8,1,1,0.0,1.0,GridBagConstraints.NORTH,GridBagConstraints.BOTH,insets,0,0));
    }
    if (buttons)
    {
      JPanel buttonPanel = new JPanel(new GridLayout(1,0));
      add(buttonPanel,new GridBagConstraints(1,9,1,1,1.0,0.0,GridBagConstraints.SOUTHWEST,GridBagConstraints.HORIZONTAL,insets,0,0));
      {
        JButton setButton = new LocalizableButton("Button.Set");
        buttonPanel.add(setButton);
        setButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            fireEvent(FCTGUIEvent.ACTION_EXPERTSETTINGS_SET);
          }
        });
      }
      {
        JButton getButton = new LocalizableButton("Button.Get");
        buttonPanel.add(getButton);
        getButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            fireEvent(FCTGUIEvent.ACTION_EXPERTSETTINGS_GET);
          }
        });
      }
    }
  }

}
