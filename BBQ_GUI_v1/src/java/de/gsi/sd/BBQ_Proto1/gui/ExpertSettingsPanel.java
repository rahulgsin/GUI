/*****************************************************************************
 *                                                                           *
 * BBQ - Expert settings panel                                               *
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
import de.gsi.sd.BBQ_Proto1.data.BBQConstants;
import de.gsi.sd.BBQ_Proto1.data.BBQExpertSettings;
import de.gsi.sd.BBQ_Proto1.data.FESAAdcSettings;
import de.gsi.sd.BBQ_Proto1.data.FESASettingsWindow;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

@SuppressWarnings("serial")
@Updatable
public class ExpertSettingsPanel extends SDPanel {

  private JComboBox samplingFrequency;
//  private JLabel[] adcLabel = new JLabel[BBQExpertSettings.ADC_COUNT];
  private JComboBox[] adcRange = new JComboBox[BBQExpertSettings.ADC_COUNT];
  private JComboBox adcValueMode;
  private JTextField filePath;
  private JComboBox fileMode;
  private JTextField modeField;
  private JTextField adcsetField1;
  private JTextField adcsetField2;
  private JTextField adcsetField3;
  private JTextField fftfecField;
  private JTextField turnfilterField;
  private JTextField offsetField;
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

  public void updateData(BBQExpertSettings settings)
  {
    readBaseline = settings.isReadBaseline();
//    samplingFrequency.setText(String.valueOf(settings.getSamplingFrequency()));
    if (settings.getSamplingFrequency() > 375)
    {
      samplingFrequency.setSelectedItem(BBQConstants.FREQUENCY[3]);
    }
    else if (settings.getSamplingFrequency() > 175)
    {
      samplingFrequency.setSelectedItem(BBQConstants.FREQUENCY[2]);
    }
    else if (settings.getSamplingFrequency() > 75)
    {
      samplingFrequency.setSelectedItem(BBQConstants.FREQUENCY[1]);
    }
    else
    {
      samplingFrequency.setSelectedItem(BBQConstants.FREQUENCY[0]);
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
    if (data instanceof BBQExpertSettings) get((BBQExpertSettings)data);
  }

  private void get(BBQExpertSettings settings)
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
    int[] range = new int[BBQExpertSettings.ADC_COUNT];
    for (int i=0;i<BBQExpertSettings.ADC_COUNT;i++)
    {
      range[i] = adcRange[i].getSelectedIndex();
    }
    settings.setAdcRange(range);
    settings.setAdcValueMode(adcValueMode.getSelectedIndex());
    settings.setFileMode(fileMode.getSelectedIndex());
    settings.setFilePath(filePath.getText());
  }
  
  public void defaultADCsettings()
  {
  	  adcsetField1.setText(String.valueOf(1));
  	  adcsetField2.setText(String.valueOf(1));
  	  adcsetField3.setText(String.valueOf(1));
  	//  adcsetField4.setText(String.valueOf(1));
  	  adcsetField1.setEditable(false);
  	  adcsetField2.setEditable(false);
  	  adcsetField3.setEditable(false);
//  	  adcsetField4.setEditable(false);
  }
  
  public void setExpertAcquisitionMode()
  {
  	  adcsetField1.setEditable(true);
  	  adcsetField2.setEditable(true);
  	  adcsetField3.setEditable(true);
//  	  adcsetField4.setEditable(true);
  }
  
  public void setFESAAdcSettings(FESAAdcSettings s)
  {
  //  modeField.setText(String.valueOf(s.getMode()));
  //  adcsetField1.setText(String.valueOf(s.getAdcSettings(0)));
   // adcsetField2.setText(String.valueOf(s.getAdcSettings(1)));
   // adcsetField3.setText(String.valueOf(s.getAdcSettings(2)));
  //  adcsetField4.setText(String.valueOf(s.getAdcSettings(3)));
  /**  rangeField.setText(String.valueOf(s.getRange()));*/
  }
  
  
  public FESAAdcSettings getFESAAdcSettings()
  {
    FESAAdcSettings s = new FESAAdcSettings();
    s.setMode(Byte.parseByte(modeField.getText()));
    s.setAdcSettings(Short.parseShort(adcsetField1.getText()),0);
    s.setAdcSettings(Short.parseShort(adcsetField2.getText()),1);
    s.setAdcSettings(Short.parseShort(adcsetField3.getText()),2);
  //   s.setAdcSettings(Short.parseShort(adcsetField4.getText()),3);
   /** s.setRange(rangeField.getText());*/
    return s;
  } 
  
  public FESASettingsWindow getFESASettingsWindow()
  {
    FESASettingsWindow s = new FESASettingsWindow();
    s.setOffset(Short.parseShort(offsetField.getText()));
   /** s.setRange(rangeField.getText());*/
    return s;
  }
  
  public void setFESASettings(FESASettingsWindow s)
  {
    offsetField.setText(String.valueOf(s.getOffset()));
  /**  rangeField.setText(String.valueOf(s.getRange()));*/
  }
  
  
  public void setStandardAcquisitionMode()
  {
  defaultADCsettings();
  }

  private void initComponents(boolean buttons)
  {
    setLayout(new GridBagLayout());
    Insets insets = new Insets(2,2,2,2);
    int  blockRow = 0;
    {
    	  JPanel fecPanel = new JPanel(new GridBagLayout());
    	  add(fecPanel,new GridBagConstraints(0,blockRow,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
    	  fecPanel.setBorder(BorderFactory.createTitledBorder("FEC"));
    	  int srow = 0;
    	  /*  {
    	    JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("Default ADC Settings");
    			GridBagConstraints gbc_rdbtnNewRadioButton_2 = new GridBagConstraints();
    			gbc_rdbtnNewRadioButton_2.insets = new Insets(0, 0, 5, 0);
    			gbc_rdbtnNewRadioButton_2.gridx = 0;
    			gbc_rdbtnNewRadioButton_2.gridy = srow;
    			fecPanel.add(rdbtnNewRadioButton_2, gbc_rdbtnNewRadioButton_2);
    			rdbtnNewRadioButton_2 .setActionCommand("enable");
    			srow++;
    			rdbtnNewRadioButton_2.addActionListener(new ActionListener() {
    		          @Override
    		          public void actionPerformed(ActionEvent e) {
    		         //   JRadioButton b = (JRadioButton)e.getSource();
    		          if ("enable".equals(e.getActionCommand())) {
    		         //  e.getActionCommand()
    		            defaultADCsettings();
    		          }
    		          else
    		          {
    		          setExpertAcquisitionMode();  
    		          }
    		        fireActionPerformed(e);
    		        }});
    	      }

    	  {
    	    fecPanel.add(new JLabel("Trigger Type:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
    	    adcsetField1 = new JTextField(2);
    	    adcsetField1.setText("1");
    	    fecPanel.add(adcsetField1,new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
    	    fecPanel.add(new JLabel("ADC Mode:"),new GridBagConstraints(2,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
    	    modeField = new JTextField(2);
    	    modeField.setText("0");
    	    fecPanel.add(modeField,new GridBagConstraints(3,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
    	    srow++;
    	  }
    	  {
    	      fecPanel.add(new JLabel("Number of Shots:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
    	      adcsetField2 = new JTextField(2);
    	      adcsetField2.setText("1");
    	      fecPanel.add(adcsetField2,new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
    	      fecPanel.add(new JLabel("Samples per Shot:"),new GridBagConstraints(2,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
    	      adcsetField3 = new JTextField(2);
    	      adcsetField3.setText("512");
    	      fecPanel.add(adcsetField3,new GridBagConstraints(3,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
    	      srow++;
    	    }
    	    */
    	    {
    	        fecPanel.add(new JLabel("FFT Length:"),new GridBagConstraints(1,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
    	        fftfecField = new JTextField(5);
    	        fftfecField.setText("1024");
    	        fecPanel.add(fftfecField,new GridBagConstraints(2,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
    	      }

    	}
    blockRow++;
    {
      JPanel adcPanel = new LocalizablePanel(new GridBagLayout());
      add(adcPanel,new GridBagConstraints(0,blockRow,1,1,1.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,insets,0,0));
      adcPanel.setBorder(BorderFactory.createTitledBorder("Title.ADC"));
      int row = 0;
      {
        LocalizableLabel label = new LocalizableLabel("Label.Range");
        adcPanel.add(label,new GridBagConstraints(2,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
        row++;
      }
      for (int i=0;i<BBQConstants.ADC_COUNT;i++)
      {
//        adcLabel[i] = new LocalizableLabel(language,BBQConstants.ADCLABELS[i]);
//        adcPanel.add(adcLabel[i],new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
        LocalizableLabel label = new LocalizableLabel(BBQConstants.ADCLABELS[i]);
        adcPanel.add(label,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
        adcRange[i] = new JComboBox(BBQConstants.ADCRANGE);
        adcPanel.add(adcRange[i],new GridBagConstraints(2,row,1,1,1.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
        row++;
      }
      {
        LocalizableLabel label = new LocalizableLabel("Label.ValueMode");
        adcPanel.add(label,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
        adcValueMode = new JComboBox(BBQConstants.ADCVALUEMODE);
        adcPanel.add(adcValueMode,new GridBagConstraints(2,row,1,1,1.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
        row++;
      }
      {
        LocalizableLabel label = new LocalizableLabel("Label.Frequency");
        adcPanel.add(label,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
        samplingFrequency = new JComboBox(BBQConstants.FREQUENCY);
        adcPanel.add(samplingFrequency,new GridBagConstraints(2,row,1,1,1.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
        row++;
      }
    }
   
   blockRow++; 
   
   if (buttons)
   {
     JPanel buttonPanel = new JPanel(new GridLayout(1,0));
     add(buttonPanel,new GridBagConstraints(0,blockRow++,1,1,1.0,0.0,GridBagConstraints.SOUTHWEST,GridBagConstraints.HORIZONTAL,insets,0,0));
     {
       JButton setButton = new LocalizableButton("Button.Set");
       buttonPanel.add(setButton);
       setButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
           fireEvent(BBQGUIEvent.ACTION_EXPERTSETTINGS_SET);
         }
       });
     }
     {
       JButton getButton = new LocalizableButton("Button.Get");
       buttonPanel.add(getButton);
       getButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
           fireEvent(BBQGUIEvent.ACTION_EXPERTSETTINGS_GET);
         }
       });
     }
   }
   {
	      JPanel fillerPanel = new JPanel(new GridBagLayout());
	      add(fillerPanel,new GridBagConstraints(0,blockRow++,1,1,0.0,1.0,GridBagConstraints.NORTH,GridBagConstraints.BOTH,insets,0,0));
	    }
    {
      JPanel filePanel = new LocalizablePanel(new GridBagLayout());
      add(filePanel,new GridBagConstraints(0,blockRow,1,1,0.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,insets,0,0));
      filePanel.setBorder(BorderFactory.createTitledBorder("Title.Output"));
      int row = 0;
      {
        LocalizableLabel label = new LocalizableLabel("Label.FileMode");
        filePanel.add(label,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        fileMode = new JComboBox(BBQConstants.FILEMODE);
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
   blockRow++;
    }
    {
        JPanel panel = new JPanel(new GridBagLayout());
  	  add(panel,new GridBagConstraints(0,blockRow++,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        panel.setBorder(BorderFactory.createTitledBorder("Data Filtering Options"));
        int srow = 0;
        {
            panel.add(new JLabel("Turn number:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
            turnfilterField = new JTextField(5);
            turnfilterField.setText("1");
            turnfilterField.setEditable(true);
            panel.add(turnfilterField,new GridBagConstraints(1,srow,2,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
            srow++;
          }
        {
          panel.add(new JLabel("Window Length:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
          offsetField = new JTextField(5);
          offsetField.setText("1024");
          panel.add(offsetField,new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
          srow++;
        }
        {
          panel.add(new JLabel("Window Type:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
          JComboBox comboBox = new JComboBox();
  		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Rectangular","Hanning", "Hamming", "Custom"}));
  		panel.add(comboBox, new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
  		 srow++;
        }
        {
        	
            panel.add(new JLabel("Spectrum Estimator:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
            JComboBox comboBox = new JComboBox();
    		comboBox.setModel(new DefaultComboBoxModel(new String[] {"FFT", "Capon", "APES"}));
    		panel.add(comboBox, new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
           // rangeField = new JTextField(5);
           // panel.add(rangeField,new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
            srow++;
        }
       // this.add(panel,BorderLayout.WEST);
      }

  }
  
  private void fireActionPerformed(ActionEvent event) 
  {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i=listeners.length-2;i>=0;i-=2) 
    {
      if (listeners[i] == ActionListener.class) 
      {
        ((ActionListener)listeners[i+1]).actionPerformed(event);
      }          
    }
  }

}
