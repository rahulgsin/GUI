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
import de.gsi.sd.BBQ_Proto1.BBQ_GUIApplication;
import de.gsi.sd.BBQ_Proto1.data.FESAAcquisitionSettings;
import de.gsi.sd.BBQ_Proto1.data.FESAAdcSettings;
import de.gsi.sd.BBQ_Proto1.data.BBQData;
import de.gsi.sd.BBQ_Proto1.data.BBQSettings;
import de.gsi.sd.BBQ_Proto1.data.FESASettingsPath;
import de.gsi.sd.BBQ_Proto1.data.FESASettingsWindow;
import de.gsi.sd.BBQ_Proto1.gui.GraphPanel;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("serial")
@Updatable
public class SettingsPanel extends SDPanel {

  private JComboBox acqModeField;
  private EventSelectorBox[] eventSelector = new EventSelectorBox[2];
  private JTextField[] delayField = new JTextField[2];
//  private JRadioButton gain0;
//  private JRadioButton gain20p;
//  private JRadioButton gain20n;
  private VirtAccSelectorBox virtAccSelector;
  private JCheckBox fileOut;
  private SpinnerNumberModel preDelayModel = new SpinnerNumberModel(0,0,320.752,4E-3);
  private SpinnerNumberModel sampleLengthModel = new SpinnerNumberModel(1024,1024,1000000,1);
 // private SpinnerListModel hwGainModel = new SpinnerListModel(HW_GAIN);
  private BBQData fesaData;
  private GraphPanel dataPanel;
  private int saveData;
  //static private final String[] TRIGGERS = { "Arm", "Trigger", "Stop", "Readout" };
  static private final String[] TRIGGERS = { "Arm", "Readout" };
 // static private final String[] HW_GAIN = { "10dB", "20dB", "30dB", "40dB", "50dB" };
  


/** Text field for the device name */
private JTextField deviceField;
/** Text field for the range value */
private JTextField rangeField;
/** Text field for the offset value */
/** Text field for the acquired value */
private JTextField valueField;
/** Text field for the offset value */
private JTextField tuneTimeField;
/** Text field for the offset value */

private JTextField datalengthField;
/** Text field for the acquired value */
//private JTextField acqModeField;
private JTextField initField;
//private JTextField delayField;
//private JTextField adcsetField4;
private JTextField dispField1;
private JTextField dispField2;
private JComboBox comboBox1;
private JComboBox comboBox2;
private String typeChannel1 = "Time";
private String typeChannel2 = "Time";

  
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

  public void updateData(BBQSettings settings)
  {
    acqModeField.setSelectedIndex(settings.getAcqMode());
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
//    hwGainModel.setValue(HW_GAIN[settings.getHwGain()]);
    virtAccSelector.setSelectedAccelerator(settings.getVAcc());
    fileOut.setSelected(settings.isFileOut());
  }

  @Override
  public void get(Object data)
  {
    if (data instanceof BBQSettings) get((BBQSettings)data);
    super.get(data);
  }

  private void get(BBQSettings settings)
  {
    settings.setAcqMode(acqModeField.getSelectedIndex());
//    settings.setSampleLength(Integer.parseInt(sampleLength.getText()));
    settings.setSampleTime(sampleLengthModel.getNumber().doubleValue());
//    settings.setPreDelay(Integer.parseInt(preDelay.getText()));
    settings.setPreDelay(preDelayModel.getNumber().doubleValue());
    int[] events = new int[2];
    long[] delay = new long[2];
    for (int i=0;i<2;i++)
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
  /*  String gain = hwGainModel.getValue().toString();
    for (int i=0;i<HW_GAIN.length;i++)
    {
      if (gain.equals(HW_GAIN[i]))
      {
        settings.setHwGain((short)i);
        break;
      }
    }*/
  //  settings.setVAcc(virtAccSelector.getSelectedAccelerator());
    settings.setFileOut(fileOut.isSelected());
  }

  
  
  
  
  private void initComponents(boolean buttons)
  {
    setLayout(new GridBagLayout());
    Insets insets = new Insets(2,2,2,2);
    int blockRow = 0;
    {
      JPanel panel = new LocalizablePanel(new GridBagLayout());
      add(panel,new GridBagConstraints(0,blockRow,1,1,0.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,insets,0,0));
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
      add(opPanel,new GridBagConstraints(0,blockRow,1,1,0.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,insets,0,0));
      opPanel.setBorder(BorderFactory.createTitledBorder("Title.Events"));
      int row = 1;
      opPanel.add(new JLabel("Event"),new GridBagConstraints(2,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
      opPanel.add(new JLabel("Delay / µs"),new GridBagConstraints(3,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
      row++;
      for (int i=0;i<2;i++)
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
      add(opPanel,new GridBagConstraints(0,blockRow,1,1,0.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,insets,0,0));
      opPanel.setBorder(BorderFactory.createTitledBorder("Title.Acquisition"));
      int row = 1;
      {
        LocalizableLabel label = new LocalizableLabel("Label.AcqMode");
        opPanel.add(label,new GridBagConstraints(0,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        acqModeField = new JComboBox(new String[] { "Multi-Event", "Single-Event" });
        opPanel.add(acqModeField,new GridBagConstraints(1,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        row++;
      }
      {
        int col = 0;
        LocalizableLabel label = new LocalizableLabel("Label.Acquisition Length");
        opPanel.add(label,new GridBagConstraints(col++,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,insets,0,0));
//        sampleLength = new JTextField(10);
        JSpinner sampleLength = new JSpinner(sampleLengthModel);
        sampleLength.setEditor(new JSpinner.NumberEditor(sampleLength,"0.0#####"));
        opPanel.add(sampleLength,new GridBagConstraints(col++,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        opPanel.add(new JLabel("Turns"),new GridBagConstraints(col++,row,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,insets,0,0));
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
      JPanel filePanel = new LocalizablePanel(new GridBagLayout());
      add(filePanel,new GridBagConstraints(0,blockRow,1,1,0.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,insets,0,0));
      filePanel.setBorder(BorderFactory.createTitledBorder("Title.Output"));
      int row = 1;
      {
//        JLabel label = new JLabel(Language.getString("Label.FileMode"));
//        filePanel.add(label,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        fileOut = new LocalizableCheckBox("Label.FileOut");
        filePanel.add(fileOut,new GridBagConstraints(0,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        row++;
      }
      blockRow++;
      /* Create the acquisition panel */
    {
    JCheckBox chckbxSaveData = new JCheckBox("Save Data");
    	filePanel.add(chckbxSaveData, new GridBagConstraints(0,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
    	chckbxSaveData.addItemListener(new ItemListener(){
    	public void itemStateChanged(ItemEvent e) {
    		saveData = 1;
          rangeField.setEditable(true);
    	    if (e.getStateChange() == ItemEvent.DESELECTED)
    	    	{
    	    	rangeField.setEditable(false);
    	    	saveData = 0;
    	    	}
    	    }});
    }
    
    {
    	
    	JComboBox comboBox = new JComboBox();	
    	comboBox.setModel(new DefaultComboBoxModel(new String[] {"Binary", "Ascii"}));
    	filePanel.add(comboBox, new GridBagConstraints(1,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
    }
    row++;
    {
        filePanel.add(new JLabel("Data Path:"),new GridBagConstraints(0,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        rangeField = new JTextField(15);
        rangeField.setEditable(false);
        filePanel.add(rangeField,new GridBagConstraints(1,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        row++;
      }
    // this.add(panel,BorderLayout.EAST); 
      /* Create the ADC settings panel */
      
    }
    if (buttons)
    {
      JPanel buttonPanel = new JPanel(new GridLayout(1,0));
      add(buttonPanel,new GridBagConstraints(0,blockRow,1,1,1.0,0.0,GridBagConstraints.SOUTHWEST,GridBagConstraints.HORIZONTAL,insets,0,0));
      {
        LocalizableButton setButton = new LocalizableButton("Button.Set");
        buttonPanel.add(setButton);
        setButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
        	  
            fireEvent(BBQGUIEvent.ACTION_SETTINGS_SET);
          }
        });
      }
      {
        LocalizableButton getButton = new LocalizableButton("Button.Get");
        buttonPanel.add(getButton);
        getButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
        	  
            fireEvent(BBQGUIEvent.ACTION_SETTINGS_GET);
          }
        });
      }
      blockRow++;
    }
    
    {
        JPanel fillerPanel = new JPanel(new GridBagLayout());
        add(fillerPanel,new GridBagConstraints(1,blockRow,1,1,0.0,1.0,GridBagConstraints.NORTH,GridBagConstraints.BOTH,insets,0,0));
        blockRow++;
      }
    /* 2 pixel space around components in the GridBagLayout */
   // Insets insets = new Insets(2,2,2,2);
    /* GridBagLayout: very flexible */ 
   // setLayout(new BorderLayout())
    //JPanel panel = new JPanel(new GridBagLayout());
   // setLayout(new GridBagLayout());
    //setPreferredSize(new Dimension(800,600));
    /* Row counter for placing panels in the GridBagLayout */
    int row = 0;
    /* Create the device panel */
  /*  {
      JPanel panel = new JPanel(new GridBagLayout());
      add(panel,new GridBagConstraints(0,blockRow++,3,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
      panel.setBorder(BorderFactory.createTitledBorder("Device"));
      int srow = 0;
      {
        panel.add(new JLabel("Device:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        deviceField = new JTextField(15);
        deviceField.setText("BbqS04");
        panel.add(deviceField,new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        JButton button = new JButton(ACTION_SUBSCRIBE);
        panel.add(button,new GridBagConstraints(2,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        button.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            JButton b = (JButton)e.getSource();
            if (e.getActionCommand().equalsIgnoreCase(ACTION_SUBSCRIBE))
              b.setText(ACTION_UNSUBSCRIBE);
            else
              b.setText(ACTION_SUBSCRIBE);
            fireActionPerformed(e);
          }
        });
        srow++;
      }
     // this.add(panel,BorderLayout.NORTH);
    }*/
    
    
 /*   {
    JPanel panel = new JPanel(new GridBagLayout());
    int srow = 0;
    {
        panel.add(new JLabel("Start Event:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        initField = new JTextField(5);
        initField.setText("32");
        panel.add(initField,new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        panel.add(new JLabel("Delay(ms):"),new GridBagConstraints(3,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
      //  delayField = new JTextField(5);
      //  delayField.setText("10");
      //  panel.add(delayField,new GridBagConstraints(4,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        srow++;
      }
    {
      panel.add(new JLabel("Acquistion Length:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
      datalengthField = new JTextField(5);
      datalengthField.setText("10000");
      panel.add(datalengthField,new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
    }
    {
        JComboBox comboBox = new JComboBox();
  		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Turns", "ms"}));
  		panel.add(comboBox, new GridBagConstraints(2,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        srow++;
    }

  //row++;
    }*/
    
    
  {
    JPanel panel = new JPanel(new GridBagLayout());
    add(panel,new GridBagConstraints(0,blockRow++,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
    panel.setBorder(BorderFactory.createTitledBorder("Display Options"));
    int srow = 0;
    {
        panel.add(new JLabel("Display Channel 1:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        dispField1 = new JTextField(5);
        dispField1.setText("1");
        panel.add(dispField1,new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        //srow++;
      }
    {
        panel.add(new JLabel("Display Channel 2:"),new GridBagConstraints(2,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        dispField2 = new JTextField(5);
        dispField2.setText("2");
        panel.add(dispField2,new GridBagConstraints(3,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        srow++;
      }

    {	
        panel.add(new JLabel("Display mode:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        comboBox1 = new JComboBox();
  		comboBox1.setModel(new DefaultComboBoxModel(new String[] {"Time", "Frequency"}));
  		panel.add(comboBox1, new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
  		comboBox1.addActionListener(new ActionListener(){
  			public void actionPerformed(ActionEvent e) {
  			        JComboBox cb = (JComboBox)e.getSource();
  			        String typeChannel = (String)cb.getSelectedItem();
  	//		        System.out.println("Selection changed");
  	//		        BBQ_GUIApplication.getLogger().info("Selection changed");
  			        setTypeChannel1(typeChannel);
  			        refreshGraphData();}
  		});
  		comboBox2 = new JComboBox();
  		comboBox2.setModel(new DefaultComboBoxModel(new String[] {"Time", "Frequency"}));
  		panel.add(comboBox2, new GridBagConstraints(3,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
  		comboBox2.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent e) {
  			        JComboBox cb = (JComboBox)e.getSource();
  			        String typeChannel = (String)cb.getSelectedItem();
  			        setTypeChannel2(typeChannel);
  			        refreshGraphData();}
  		});
        srow++;
    }
    {
        panel.add(new JLabel("Spectrum number:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        tuneTimeField = new JTextField(5);
        tuneTimeField.setText("1");
        tuneTimeField.setEditable(true);
        panel.add(tuneTimeField,new GridBagConstraints(1,srow,2,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        srow++;
        tuneTimeField.addActionListener(new ActionListener() {
     			public void actionPerformed(ActionEvent e) {
     			refreshGraphData(Integer.parseInt(tuneTimeField.getText()));}
     		});
        }
  //row++;
  }
  
   
 
    
  {
        JPanel panel = new JPanel(new GridBagLayout());
       // add(panel,new GridBagConstraints(0,row++,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
       // panel.setBorder(BorderFactory.createTitledBorder("Data Settings"));
        int srow = 0;
        {
        JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Default Acquistion Settings");
  		GridBagConstraints gbc_rdbtnNewRadioButton_1 = new GridBagConstraints();
  		gbc_rdbtnNewRadioButton_1.insets = new Insets(0, 0, 5, 0);
  		gbc_rdbtnNewRadioButton_1.gridx = 0;
  		gbc_rdbtnNewRadioButton_1.gridy = srow;
  		panel.add(rdbtnNewRadioButton_1, gbc_rdbtnNewRadioButton_1);
  		srow++;
  		rdbtnNewRadioButton_1 .setActionCommand("enable");
  		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
  	          @Override
  	          public void actionPerformed(ActionEvent e) {
  	         //   JRadioButton b = (JRadioButton)e.getSource();
  	          if ("enable".equals(e.getActionCommand())) {
  	         //  e.getActionCommand()
  	            defaultAcquisitionsettings();
  	          }
  	        }});
    
  }

}
}



/**
 * Adds an <code>ActionListener</code> to the main panel.
 * @param l the <code>ActionListener</code> to be added
 */
public void addActionListener(ActionListener l) 
{
  listenerList.add(ActionListener.class, l);
}

/**
 * Removes an <code>ActionListener</code> from the main panel.
 * @param l the listener to be removed
 */
public void removeActionListener(ActionListener l) 
{
  listenerList.remove(ActionListener.class, l);
}

/**
 * Get the name of the FESA device from the text field
 * @return the FESA device name
 */

public String getDevice()
{
  return deviceField.getText();
}

public int getChannel1()
{
	  return Integer.parseInt(dispField1.getText());
}

public int getChannel2()
{
	  return Integer.parseInt(dispField2.getText());
}
public void setTypeChannel1(String typeChannel)
{
	 this.typeChannel1= typeChannel;
}

public void setTypeChannel2(String typeChannel)
{
	this.typeChannel2= typeChannel; 
}

public int getTypeChannel1()
{
	  String text = this.typeChannel1;
	  int type = 0;
	  if (text == "Time")
		  type = 0;
		  else
		  type = 1;
	  return type;
}

public int getTypeChannel2()
{
	  String text = this.typeChannel2;
	  int type = 0;
	  if (text == "Time")
		  type = 0;
		  else
		  type = 1;
	  return type;
}

public int getSpectrumNumber()
{
	  
	  return Integer.parseInt(tuneTimeField.getText());
}


/**
 * Display the acquired FESA data in the value field.
 * @param d the FESA data
 * @throws IOException 
 */
public void setFESAData(BBQData d, GraphPanel dataPanel) throws IOException
{
	this.dataPanel = dataPanel;
	BBQ_GUIApplication.getLogger().info("Settings Panel:: Setting Data");
	this.fesaData = d.getCurrentFESAData();
	int [] channel ={getChannel1(),getChannel2()};
	int [] type ={getTypeChannel1(),getTypeChannel2()};
 // valueField.setText(Arrays.toString(d.getTime(getChannel1())));
  this.dataPanel.updateData(d,channel,type);
  if (this.saveData == 1)
  {

	   try { 
	        FileWriter file = new FileWriter("/home/Rahul/Desktop/binary.data"); 
	        BufferedWriter buffer = new BufferedWriter(file);
	        double [] tempDoubleArray = d.getTime();
	        for (int i =0;i< tempDoubleArray.length;i++)
	        buffer.write((int)tempDoubleArray[i]); /* you had this as sDividend1 which was not declared */
	    } catch(Exception ex) {
	        //catch logic here
	    }
  }
}



public void refreshGraphData()
{
	  int [] channel ={getChannel1(),getChannel2()};
	  int [] type ={getTypeChannel1(),getTypeChannel2()};
	  dataPanel.updateData(this.fesaData,channel,type);
}

public void refreshGraphData(int specNumber)
{
//	  System.out.println("J Text Action");
	  this.fesaData.setSpecNumber(specNumber);
	  this.fesaData.setPartialFreData(specNumber);
	  int [] channel ={getChannel1(),getChannel2()};
	  int [] type ={getTypeChannel1(),getTypeChannel2()};
	  dataPanel.updateData(this.fesaData,channel,type,specNumber);
}
/**
 * Get the FESA settings from the range and offset fields.
 * @return the FESA settings
 */
public FESASettingsWindow getFESASettingsWindow()
{
  FESASettingsWindow s = new FESASettingsWindow();
//  s.setOffset(Short.parseShort(offsetField.getText()));
 /** s.setRange(rangeField.getText());*/
  return s;
}

public FESASettingsPath getFESASettingsPath()
{
  FESASettingsPath s = new FESASettingsPath();
  s.setRange(rangeField.getText());
 /** s.setRange(rangeField.getText());*/
  return s;
} 


public FESAAcquisitionSettings getFESAAcquisitionSettings()
{
  FESAAcquisitionSettings s = new FESAAcquisitionSettings();
//  s.setMode(Byte.parseByte(acqModeField.getText()));
//  s.setAcquisitionSettings(Short.parseShort(initField.getText()),0);
//  s.setAcquisitionSettings(Short.parseShort(datalengthField.getText()),1);
  
 // settings.setAcqMode(acqModeField.getSelectedIndex());
  s.setAcqMode(acqModeField.getSelectedIndex());
//settings.setSampleLength(Integer.parseInt(sampleLength.getText()));
//settings.setSampleTime(sampleLengthModel.getNumber().doubleValue());
  s.setSampleTime(sampleLengthModel.getNumber().doubleValue());
//settings.setPreDelay(Integer.parseInt(preDelay.getText()));
//settings.setPreDelay(preDelayModel.getNumber().doubleValue());
  s.setPreDelay(preDelayModel.getNumber().doubleValue());
int[] events = new int[2];
long[] delay = new long[2];
for (int i=0;i<2;i++)
{
  int index = eventSelector[i].getSelectedIndex();
  events[i] = EventList.getEventNumbers()[index];
  delay[i] = (long)(Double.parseDouble(delayField[i].getText()) * 1000.0);
}
s.setTriggerDelays(delay);
s.setTriggerEvents(events);
  
s.setAcquisitionSettings(virtAccSelector.getSelectedAccelerator(),2);
//s.setFileOut(fileOut.isSelected());
 // s.setAcquisitionSettings(Short.parseShort(delayField.getText()),2);
//   s.setAdcSettings(Short.parseShort(adcsetField4.getText()),3);
 /** s.setRange(rangeField.getText());*/
  return s;
} 
/**
 * Display the FESA settings in the range and offset fields.
 * @param s the FESA settings
 */
public void setFESASettings(FESASettingsWindow s)
{
 // offsetField.setText(String.valueOf(s.getOffset()));
/**  rangeField.setText(String.valueOf(s.getRange()));*/
}


public void setFESASettings(FESASettingsPath s)
{
  rangeField.setText(s.getRange());
/**  rangeField.setText(String.valueOf(s.getRange()));*/
}


public void setFESAAcquisitionSettings(FESAAcquisitionSettings s)
{
//	acqModeField.setText(String.valueOf(s.getMode()));
 // initField.setText(String.valueOf(s.getAcquisitionSettings(0)));
 // datalengthField.setText(String.valueOf(s.getAcquisitionSettings(1)));
  acqModeField.setSelectedIndex(s.getAcqMode());
  sampleLengthModel.setValue(s.getSampleTime());
  preDelayModel.setValue(s.getPreDelay());
  for (int i=0;i<s.getTriggerEvents().length;i++)
  {
    int evt = s.getTriggerEvents()[i];
    eventSelector[i].setSelectedIndex(EventList.getEventIndex(evt));
    delayField[i].setText(String.valueOf((s.getTriggerDelays()[i]/1000.0)));
  }
//  if (settings.getHwGain() < 0)
//    gain20n.setSelected(true);
//  else if (settings.getHwGain() > 0)
//    gain20p.setSelected(true);
//  else
//    gain0.setSelected(true);
//  hwGainModel.setValue(HW_GAIN[settings.getHwGain()]);
  virtAccSelector.setSelectedAccelerator((short) s.getAcquisitionSettings(2));
 // fileOut.setSelected(settings.isFileOut());
  
//  delayField.setText(String.valueOf(s.getAcquisitionSettings(2)));
//  adcsetField4.setText(String.valueOf(s.getAdcSettings(3)));
/**  rangeField.setText(String.valueOf(s.getRange()));*/
}


/**
 * Create all GUI components on the main panel
 */
private void initGUI()
{


      }
      /*   {
          panel.add(new JLabel("Acquisition Mode:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
          acqModeField = new JTextField(2);
          acqModeField.setText("0");
          panel.add(acqModeField,new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
          srow++;
        } 
        */


public void setStandardAcquisitionMode()
{
defaultAcquisitionsettings();
}

public void defaultAcquisitionsettings()
{
	  initField.setText(String.valueOf(32));
//	  delayField.setText(String.valueOf(10));
//	  adcsetField3.setText(String.valueOf(1));
	//  adcsetField4.setText(String.valueOf(1));  
}
/**
 * Notifies all listeners that have registered interest for
 * notification on this event type.  
 * @param event  the <code>ActionEvent</code> object
 */
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

