/*****************************************************************************
 *                                                                           *
 * BBQ GUI - Main panel                                               *
 *                                                                           *
 * modified: 2013-11-13 Rahul Singh                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.gui;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import de.gsi.sd.BBQ_Proto1.BBQ_GUIApplication;
import de.gsi.sd.BBQ_Proto1.data.FESAAcquisitionSettings;
import de.gsi.sd.BBQ_Proto1.data.FESAAdcSettings;
import de.gsi.sd.BBQ_Proto1.data.FESAData;
import de.gsi.sd.BBQ_Proto1.data.FESASettingsWindow;
import de.gsi.sd.BBQ_Proto1.data.FESASettingsPath;
import java.util.Arrays;

import de.gsi.sd.common.controls.AbstractMainPanel;
import de.gsi.sd.common.controls.PrintPages;
import de.gsi.sd.common.controls.Updatable;
import de.gsi.sd.common.controls.localizable.LocalizableButton;
import de.gsi.sd.common.language.Language;
import de.gsi.sd.BBQ_Proto1.data.FCTStatus;
import de.gsi.sd.BBQ_Proto1.gui.GraphPanel;
import de.gsi.sd.BBQ_Proto1.gui.StatusPanel;
import de.gsi.sd.BBQ_Proto1.gui.TabbedControlsPanel;
//import de.gsi.sd.fct.data.FCTStatus; /* I am using FCT GUI code*/

import java.io.File;
import org.apache.log4j.Logger;
/**
 * The MainPanel is the central GUI panel of the application. It contains
 * all relevant GUI elements. 
 */
@SuppressWarnings("serial")
@Updatable
public class MainPanel extends AbstractMainPanel {

//  private StatusPanel statusPanel;
  private GraphPanel dataPanel;
 // private TabbedControlsPanel tabPanel;
 // private JPanel buttonPanel;
  //private JPanel controlsPanel;
 // private JButton subscribeButton;
 // private JButton startButton;
  private FESAData fesaData;

  @SuppressWarnings("unused")
  static private Logger logger = Logger.getLogger(MainPanel.class);



 /*  public void updateStatus(FCTStatus status)
  {
    setRunning(!status.isStopped());
  }*/



  @Override
  public PrintPages getPrintPages()
  {
    return dataPanel.getPrintPages();
  }
  
  @Override
  public boolean export(File file, String format)
  {
    return dataPanel.export(file,format);
  }
  
 /* public boolean isControlsVisible()
  {
    return controlsPanel.isVisible();
  }
  
  public void setControlsVisible(boolean flag)
  {
    controlsPanel.setVisible(flag);
  }
  
  public boolean isStatusVisible()
  {
    return statusPanel.isVisible();
  }
  
  public void setStatusVisible(boolean flag)
  {
    statusPanel.setVisible(flag);
  }
  
  public void setSubscribed(boolean flag)
  {
    if (flag)
    {
      subscribeButton.setText("Button.Unsubscribe");
    }
    else
    {
      subscribeButton.setText("Button.Subscribe");
    }
  }

  public void setRunning(boolean flag)
  {
    if (flag)
    {
      startButton.setText("Button.Stop");
    }
    else
    {
      startButton.setText("Button.Start");
    }
  }*/

/*@SuppressWarnings("serial")
 public class MainPanel extends JPanel {*/

  /* Name of the subscribe action */
  static public final String ACTION_SUBSCRIBE   = "Subscribe";
  /** Name of the unsubscribe action */
  static public final String ACTION_UNSUBSCRIBE = "Unsubscribe"; 

  /** Text field for the device name */
  private JTextField deviceField;
  /** Text field for the range value */
  private JTextField rangeField;
  /** Text field for the offset value */
  private JTextField offsetField;
  /** Text field for the acquired value */
  private JTextField valueField;
  /** Text field for the offset value */
  private JTextField modeField;
  private JTextField datalengthField;
  /** Text field for the acquired value */
  private JTextField initField;
  private JTextField delayField;
  private JTextField adcsetField1;
  private JTextField adcsetField2;
  private JTextField adcsetField3;
  //private JTextField adcsetField4;
  private JTextField dispField1;
  private JTextField dispField2;
  private JTextField tunexField;
  private JTextField tuneyField;
  private JTextField couplingField;
  private JTextField deltaField;
  private JComboBox comboBox1;
  private JComboBox comboBox2;
  private String typeChannel1;
  private String typeChannel2;

  /**
   * Constructor for the main panel
   */
  public MainPanel()
  {
    super();
    initGUI();
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
  
  /**
   * Display the acquired FESA data in the value field.
   * @param d the FESA data
   */
  public void setFESAData(FESAData d)
  {
	this.fesaData = d.getCurrentFESAData();
	int [] channel ={getChannel1(),getChannel2()};
	int [] type ={getTypeChannel1(),getTypeChannel2()};
    valueField.setText(Arrays.toString(d.getTime(getChannel1())));
    dataPanel.updateData(d,channel,type);
  }
  
  public void refreshGraphData()
  {
	  int [] channel ={getChannel1(),getChannel2()};
	  int [] type ={getTypeChannel1(),getTypeChannel2()};
	  dataPanel.updateData(this.fesaData,channel,type);
  }
  /**
   * Get the FESA settings from the range and offset fields.
   * @return the FESA settings
   */
  public FESASettingsWindow getFESASettingsWindow()
  {
    FESASettingsWindow s = new FESASettingsWindow();
    s.setOffset(Short.parseShort(offsetField.getText()));
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
  
  public FESAAcquisitionSettings getFESAAcquisitionSettings()
  {
    FESAAcquisitionSettings s = new FESAAcquisitionSettings();
    s.setAcquisitionSettings(Short.parseShort(initField.getText()),0);
    s.setAcquisitionSettings(Short.parseShort(datalengthField.getText()),1);
    s.setAcquisitionSettings(Short.parseShort(delayField.getText()),2);
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
    offsetField.setText(String.valueOf(s.getOffset()));
  /**  rangeField.setText(String.valueOf(s.getRange()));*/
  }
  
  
  public void setFESASettings(FESASettingsPath s)
  {
    rangeField.setText(s.getRange());
  /**  rangeField.setText(String.valueOf(s.getRange()));*/
  }
  
  public void setFESAAdcSettings(FESAAdcSettings s)
  {
    modeField.setText(String.valueOf(s.getMode()));
    adcsetField1.setText(String.valueOf(s.getAdcSettings(0)));
    adcsetField2.setText(String.valueOf(s.getAdcSettings(1)));
    adcsetField3.setText(String.valueOf(s.getAdcSettings(2)));
  //  adcsetField4.setText(String.valueOf(s.getAdcSettings(3)));
  /**  rangeField.setText(String.valueOf(s.getRange()));*/
  }
  
  public void setFESAAcquisitionSettings(FESAAcquisitionSettings s)
  {
    initField.setText(String.valueOf(s.getAcquisitionSettings(0)));
    datalengthField.setText(String.valueOf(s.getAcquisitionSettings(1)));
    delayField.setText(String.valueOf(s.getAcquisitionSettings(2)));
  //  adcsetField4.setText(String.valueOf(s.getAdcSettings(3)));
  /**  rangeField.setText(String.valueOf(s.getRange()));*/
  }
  

  /**
   * Create all GUI components on the main panel
   */
  private void initGUI()
  {

    /* 2 pixel space around components in the GridBagLayout */
    Insets insets = new Insets(2,2,2,2);
    /* GridBagLayout: very flexible */ 
   // setLayout(new BorderLayout())
    //JPanel panel = new JPanel(new GridBagLayout());
    setLayout(new GridBagLayout());
    //setPreferredSize(new Dimension(800,600));
    /* Row counter for placing panels in the GridBagLayout */
    int row = 0;
    /* Create the device panel */
    {
      JPanel panel = new JPanel(new GridBagLayout());
      add(panel,new GridBagConstraints(0,row++,3,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
      panel.setBorder(BorderFactory.createTitledBorder("Device"));
      int srow = 0;
      {
        panel.add(new JLabel("Device:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        deviceField = new JTextField(15);
        deviceField.setText("TestRsBbqPrototype2");
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
    }
    
    {
        dataPanel = new GraphPanel();
        add(dataPanel,new GridBagConstraints(2,1,1,4,2.0,2.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
      //  add(dataPanel,BorderLayout.CENTER);
      }
    /* Create the ADC settings panel */
    {
    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	//tabbedPane.setBounds(26, 38, 189, 172);
        /* Create the data settings panel */
	add(tabbedPane,new GridBagConstraints(0,row++,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
    {
        JPanel panel = new JPanel(new GridBagLayout());
  	  tabbedPane.addTab("Acquistion Settings", null, panel, null);
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
        {
            panel.add(new JLabel("Acquisition Mode:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
            modeField = new JTextField(2);
            modeField.setText("0");
            panel.add(modeField,new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
            srow++;
          } 
        {
            panel.add(new JLabel("Start Event:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
            initField = new JTextField(5);
            initField.setText("32");
            panel.add(initField,new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
            panel.add(new JLabel("Delay(ms):"),new GridBagConstraints(3,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
            delayField = new JTextField(5);
            delayField.setText("10");
            panel.add(delayField,new GridBagConstraints(4,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
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

    }
        {
      JPanel panel = new JPanel(new GridBagLayout());
      tabbedPane.addTab("ADC Settings", null, panel, null);
      //add(panel,new GridBagConstraints(0,row++,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
      //panel.setBorder(BorderFactory.createTitledBorder("ADC Settings"));
      int srow = 0;
      {
        JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("Default ADC Settings");
  		GridBagConstraints gbc_rdbtnNewRadioButton_2 = new GridBagConstraints();
  		gbc_rdbtnNewRadioButton_2.insets = new Insets(0, 0, 5, 0);
  		gbc_rdbtnNewRadioButton_2.gridx = 0;
  		gbc_rdbtnNewRadioButton_2.gridy = srow;
  		panel.add(rdbtnNewRadioButton_2, gbc_rdbtnNewRadioButton_2);
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
        panel.add(new JLabel("Trigger Type:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        adcsetField1 = new JTextField(2);
        adcsetField1.setText("1");
        panel.add(adcsetField1,new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        srow++;
      }
      {
          panel.add(new JLabel("Number of Shots:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
          adcsetField2 = new JTextField(2);
          adcsetField2.setText("1");
          panel.add(adcsetField2,new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
          srow++;
      }
      {
          panel.add(new JLabel("Samples per Shot:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
          adcsetField3 = new JTextField(2);
          adcsetField3.setText("512");
          panel.add(adcsetField3,new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
          srow++;
        }


    }
    }
  //  row++;
    {
        JPanel panel = new JPanel(new GridBagLayout());
  	  add(panel,new GridBagConstraints(0,row++,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
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
    		comboBox1.setModel(new DefaultComboBoxModel(new String[] {"Frequency", "Time"}));
    		panel.add(comboBox1, new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
    		comboBox1.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    			        JComboBox cb = (JComboBox)e.getSource();
    			        String typeChannel = (String)cb.getSelectedItem();
    			        System.out.println("Selection changed");
    			        BBQ_GUIApplication.getLogger().info("Selection changed");
    			        setTypeChannel1(typeChannel);
    			        refreshGraphData();}
    		});
    		comboBox2 = new JComboBox();
    		comboBox2.setModel(new DefaultComboBoxModel(new String[] {"Frequency", "Time"}));
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
      }

        {
            JPanel panel = new JPanel(new GridBagLayout());
      	  add(panel,new GridBagConstraints(0,row++,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
            panel.setBorder(BorderFactory.createTitledBorder("Data Filtering Options"));
            int srow = 0;
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
   
    /* Create the acquisition panel */
    {
      JPanel panel = new JPanel(new GridBagLayout());
      add(panel,new GridBagConstraints(0,row++,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
      panel.setBorder(BorderFactory.createTitledBorder("Results"));
      int srow = 0;
      {
        panel.add(new JLabel("Acquired Time Sequence:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        valueField = new JTextField(10);
        valueField.setEditable(false);
        panel.add(valueField,new GridBagConstraints(1,srow,3,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        srow++;
      }
      {
        panel.add(new JLabel("Tune X (Qx):"),new GridBagConstraints(0,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        tunexField = new JTextField(5);
        tunexField.setEditable(false);
        panel.add(tunexField,new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        panel.add(new JLabel("Tune Y (Qy):"),new GridBagConstraints(2,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        tuneyField = new JTextField(5);
        tuneyField.setEditable(false);
        panel.add(tuneyField,new GridBagConstraints(3,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        srow++;
      }
      {
        panel.add(new JLabel("Coupling (C-):"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        couplingField = new JTextField(5);
        couplingField.setEditable(false);
        panel.add(couplingField,new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        panel.add(new JLabel("Delta:"),new GridBagConstraints(2,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        deltaField = new JTextField(5);
        deltaField.setEditable(false);
        panel.add(deltaField,new GridBagConstraints(3,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        srow++;
      }
    
      {
      JCheckBox chckbxSaveData = new JCheckBox("Save Data");
		panel.add(chckbxSaveData, new GridBagConstraints(0,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
		chckbxSaveData.addItemListener(new ItemListener(){
		public void itemStateChanged(ItemEvent e) {
	//	    Object source = e.getItemSelectable();
    //       JCheckBox b = (JCheckBox)e.getSource();
            rangeField.setEditable(true);
		    if (e.getStateChange() == ItemEvent.DESELECTED)
		    	{
		    	rangeField.setEditable(false);
		    	}
		    }});
		
		JComboBox comboBox = new JComboBox();	
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Binary", "Ascii"}));
		panel.add(comboBox, new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
      }
      srow++;
      {
          panel.add(new JLabel("Data Path:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
          rangeField = new JTextField(15);
          rangeField.setEditable(false);
          panel.add(rangeField,new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
          srow++;
        }
     // this.add(panel,BorderLayout.EAST); 
    }
  }
  
  public void setStandardAcquisitionMode()
  {
defaultADCsettings();
defaultAcquisitionsettings();
  }
  
  public void setExpertAcquisitionMode()
  {
	  adcsetField1.setEditable(true);
	  adcsetField2.setEditable(true);
	  adcsetField3.setEditable(true);
//	  adcsetField4.setEditable(true);
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
//	  adcsetField4.setEditable(false);
  }
  
  public void defaultAcquisitionsettings()
  {
	  initField.setText(String.valueOf(32));
	  delayField.setText(String.valueOf(10));
	  adcsetField3.setText(String.valueOf(1));
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
