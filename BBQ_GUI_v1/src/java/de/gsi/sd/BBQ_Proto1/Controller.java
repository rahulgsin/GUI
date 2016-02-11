/*****************************************************************************
 *                                                                           *
 * BBQ - Application controller                                              *
 *                                                                           *
 * modified: 2010-08-04 Rahul Singh                                          *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

import cern.accsoft.gui.beans.HtmlReaderPanel;
import cern.accsoft.gui.frame.DialogManager;
import cern.accsoft.gui.frame.ExternalFrame;
import cern.accsoft.gui.frame.FrameManager;
import cern.japc.ParameterException;
import cern.jdve.utils.DataRange;
import de.gsi.sd.BBQ_Proto1.data.FESAAcquisitionSettings;
import de.gsi.sd.BBQ_Proto1.data.FESASettingsPath;
import de.gsi.sd.BBQ_Proto1.data.FESAAdcSettings;
import de.gsi.sd.BBQ_Proto1.data.FESASettingsWindow;
import de.gsi.sd.BBQ_Proto1.data.provider.JAPCDataProvider;
import de.gsi.sd.BBQ_Proto1.data.provider.JAPCSettingsProvider;
import de.gsi.sd.BBQ_Proto1.dialogs.AboutDialog;
import de.gsi.sd.BBQ_Proto1.gui.MainPanel;
import de.gsi.sd.BBQ_Proto1.gui.SettingsPanel;
import de.gsi.sd.BBQ_Proto1.gui.ExpertSettingsPanel;
import de.gsi.sd.BBQ_Proto1.gui.BBQGUIEvent;
import de.gsi.sd.BBQ_Proto1.gui.TabbedControlsPanel;
import de.gsi.sd.common.controls.ControllerAction;
import de.gsi.sd.common.controls.GUIEvent;
import de.gsi.sd.common.controls.GUIListener;
import de.gsi.sd.common.japc.GenericProvider;
import de.gsi.sd.common.japc.ProviderEvent;
import de.gsi.sd.common.japc.ProviderListener;
import de.gsi.sd.common.japc.StatusProvider;
import de.gsi.sd.common.language.Language;
import de.gsi.sd.common.oper.OPERDB;
import de.gsi.sd.common.oper.OPERDBData;
import de.gsi.sd.common.oper.OPERDBEvent;
import de.gsi.sd.common.oper.OPERDBListener;
import de.gsi.sd.BBQ_Proto1.gui.dialogs.DataProviderReductionDialog;
import de.gsi.sd.BBQ_Proto1.gui.dialogs.ExpertSettingsDialog;
import de.gsi.sd.BBQ_Proto1.gui.dialogs.ExternalChangeDialog;
import de.gsi.sd.BBQ_Proto1.gui.dialogs.SettingsDialog;
import de.gsi.sd.BBQ_Proto1.data.BBQData;
import de.gsi.sd.BBQ_Proto1.gui.GraphPanel;
import de.gsi.sd.BBQ_Proto1.data.BBQExpertSettings;
import de.gsi.sd.BBQ_Proto1.data.BBQSettings;
import de.gsi.sd.BBQ_Proto1.data.BBQStatus;
import de.gsi.sd.BBQ_Proto1.data.provider.ControlProvider;
import de.gsi.sd.BBQ_Proto1.gui.BBQGUIEvent;
/**
 * The application controller class is the central class of the application. It
 * receives action events from the GUI (i.e. main panel), data from the
 * providers, sends data to the GUI and handles the menu bar and related 
 * actions.
 * To receive action events from the GUI, the controller class implements the
 * ActionListener interface. This is used to handle the Subscribe button on the 
 * main panel.
 * To receive data asynchronously from the JAPCDataProvider, the controller class implements the 
 * JAPCDataProviderInterface. Whenever the data provider obtains new data, this
 * class will be informed. No such mechanism is used for the settings provider as
 * settings are acquired synchronously on demand.
 */
public class Controller implements ActionListener, ProviderListener { // removed action listener

  private String device;
  private ExternalFrame frame;
  private MainPanel mainPanel;
  private SettingsPanel settingsPanel;
  private GraphPanel dataPanel;
  private ExpertSettingsPanel expertSettingsPanel;
  private JAPCDataProvider dataProvider;
  private StatusProvider statusProvider;
  private ControlProvider controlProvider;
  private GenericProvider settingsProvider;
  private GenericProvider expertSettingsProvider;
  private BBQData data;
  private OPERDBData operDBData;
  
  private BBQSettings bbqSettings = new BBQSettings();
  private BBQExpertSettings bbqExpertSettings = new BBQExpertSettings();
  
  private JCheckBoxMenuItem subscribeMenuItem;
  private JCheckBoxMenuItem measurementMenuItem;
  
  private boolean expectSettings = false;
  private boolean expectExpertSettings = false;
  
  
  static private Logger logger = Logger.getLogger(Controller.class);
  /**
   * Constructor for the Controller object. 
   * @param frame the application main frame
   * @param mainPanel the GUI main panel
   */
  public Controller(String device, ExternalFrame frame, MainPanel mainPanel)
  {
    this.frame = frame;
    this.device = device;
    this.mainPanel = mainPanel;
    /* Add this class as action listener to the main panel (for the subscribe button) */
    mainPanel.addListener(new GUIHandler());
    /* Create the menu bar */
    createMenuBar();
    /* Create the data provider and register as a listener */
    initProviders();
  }

  
  
  private void initProviders()
  {
    controlProvider = new ControlProvider(device);
    
    settingsProvider = new GenericProvider(device,"Setting",null);
    settingsProvider.setPropertyClass(BBQSettings.class);
    settingsProvider.addListener(this);
    
    expertSettingsProvider = new GenericProvider(device,"ExpertSetting",null);
    expertSettingsProvider.setPropertyClass(BBQExpertSettings.class);
    expertSettingsProvider.addListener(this);
    
    if (OPERDB.getInstance().isConnected())
    {
      OPERDB.getInstance().addListener(new OperDBHandler());
      operDBData = OPERDB.getInstance().getData();
    }
    else
    {
      OPERDB.getInstance().stopAutoUpdate();
      DialogManager.showWarningDialog("Connection to OperDB failed! No beam info will be available.");
    }
    
    dataProvider = new JAPCDataProvider(device,"Acquisition",null);
    dataProvider.addListener(this);
   // dataProvider = new DataProvider(device,"Acquisition",null);
   // dataProvider.addListener(this);
    
    statusProvider = new StatusProvider(device);
    statusProvider.setPropertyClass(BBQStatus.class);
    statusProvider.addListener(this);
    
  /*  try
    {
      statusProvider.subscribe();
    }
    catch (ParameterException e)
    {
      DialogManager.showErrorDialog("Status provider subscription failed. FEC not reachable. Will terminate!",e);
      //System.exit(0);
    }

    try
    {
      settingsProvider.subscribe();
      expertSettingsProvider.subscribe();
      // supress message about settings changed externally 
      expectSettings = true;
      expectExpertSettings = true;
      settingsProvider.get(bbqSettings);
      expertSettingsProvider.get(bbqExpertSettings);
    }
    catch (ParameterException ex)
    {
      logger.error("Settings provider subscription failed",ex);
    }*/
    controlProvider.powerOn();
    
    TabbedControlsPanel tabbedControlsPanels = (TabbedControlsPanel)mainPanel.findPanel(TabbedControlsPanel.class);
    settingsPanel = (SettingsPanel)tabbedControlsPanels.findPanel(SettingsPanel.class);
    expertSettingsPanel = (ExpertSettingsPanel)tabbedControlsPanels.findPanel(ExpertSettingsPanel.class);
    dataPanel = (GraphPanel)mainPanel.findPanel(GraphPanel.class);
  }

  public void exitBBQ()
  {
//    if (dataProvider.isSubscribed()) dataProvider.unsubscribe();
    if (settingsProvider.isSubscribed()) settingsProvider.unsubscribe();
    if (expertSettingsProvider.isSubscribed()) expertSettingsProvider.unsubscribe();
    if (statusProvider.isSubscribed()) statusProvider.unsubscribe();
    if (DialogManager.showConfirmDialog(Language.getInstance().getString("Label.PowerOffDevice")) == DialogManager.YES_OPTION)
    {
      controlProvider.powerOff();
    }
    System.exit(0);
  }
  /**
   * Perform an action specified by the action event
   * @param e the action event
   */
  @Override
  public void actionPerformed(ActionEvent e) 
  {
//    if (e.getActionCommand().equals(MainPanel.ACTION_SUBSCRIBE))
//      dataProvider.subscribe(mainPanel.getDevice());
//    else if (e.getActionCommand().equals(MainPanel.ACTION_UNSUBSCRIBE))
//      dataProvider.unsubscribe();
  }
  
  /**
   * Handle the reception of data from the data provider.
   * @param data the FESA data received
   */

  /**
   * Terminate the application
   */
  public void exitApp()
  {
    System.exit(0);
  }

  /**
   * Display the about dialog
   */
  public void about()
  {
    AboutDialog d = new AboutDialog(frame.getJFrame());
    d.setVisible(true);
  }

  /**
   * Display the help browser
   */
  public void help()
  {
    /* Create the HTML reader panel */
    HtmlReaderPanel readerPanel = new HtmlReaderPanel();
    /* Set a reasonable preferred size for the reader panel */
    readerPanel.setPreferredSize(new Dimension(800,600));
    /* Obtain the server for help pages */
    String htmlServer = System.getProperty("SDHelpSrv","http://sdlx014.acc.gsi.de/");
    /* There is no actual help page for this application. We just display the SD
     * FESA web page as an example. */
    htmlServer += "FesaWeb/primers.html";
    /* Set the URL of the help page */
    readerPanel.setHomeUrl(htmlServer);
    /* Create an external frame for the reader panel */
    ExternalFrame f = FrameManager.getInstance().createExternalFrame(null);
    f.setRootComponent(readerPanel);
    f.setTitle("Help");
    /* Display the help browser */
    f.setVisible(true);
  }
  /**
   * Set the Setting property of the FESA device
   */
  public void setStandardAcquistionMode()
  {
    /* Get device name from the main panel */
   // String deviceName = settingsPanel.getDevice();
	  String deviceName = this.device;
    if (deviceName == null || deviceName.isEmpty())
    {
      DialogManager.showErrorDialog("Please specify a device first!");
      return;
    }
    /* Get the FESA settings from the main panel */
    settingsPanel.setStandardAcquisitionMode();
  }
  
  public void setExpertAcquistionMode()
  {
    /* Get device name from the main panel */
    //String deviceName = settingsPanel.getDevice();
	  String deviceName = this.device;
    if (deviceName == null || deviceName.isEmpty())
    {
      DialogManager.showErrorDialog("Please specify a device first!");
      return;
    }
    /* Get the FESA settings from the main panel */
    expertSettingsPanel.setExpertAcquisitionMode();
  }
  /**
   * Set the Setting property of the FESA device
   */
  public void setSettings()
  {
    /* Get device name from the main panel */
    //String deviceName = settingsPanel.getDevice();
	  String deviceName = this.device;
    if (deviceName == null || deviceName.isEmpty())
    {
      DialogManager.showErrorDialog("Please specify a device first!");
      return;
    }
    /* Get the FESA settings from the main panel */
    FESASettingsWindow settingswin = settingsPanel.getFESASettingsWindow();
    FESASettingsPath settingspath = settingsPanel.getFESASettingsPath();
   // FESAAdcSettings adcsettings = expertSettingsPanel.getFESAAdcSettings();
    FESAAcquisitionSettings acquisitionsettings = settingsPanel.getFESAAcquisitionSettings();
    /* Set the settings via the JAPCSettingsProvider */
 //   JAPCSettingsProvider.setWindow(deviceName,settingswin);
    JAPCSettingsProvider.setPath(deviceName,settingspath);
  //  JAPCSettingsProvider.setAdcSettings(deviceName,adcsettings);
    JAPCSettingsProvider.setAcquisitionSettings(deviceName,acquisitionsettings);
  }
  
  /**
   * Get the FESA settings from the device
   */
  public void getSettings()
  {
    /* Get device name from the main panel */
   // String deviceName = settingsPanel.getDevice();
	  String deviceName = this.device;
    if (deviceName == null || deviceName.isEmpty())
    {
      DialogManager.showErrorDialog("Please specify a device first!");
      return;
    }
    /* Get the FESA settings from the device via the JAPCSettingsProvider */
  //  FESASettingsWindow settingswin = JAPCSettingsProvider.getWindow(deviceName);
    FESASettingsPath settingspath = JAPCSettingsProvider.getPath(deviceName);
  //  FESAAdcSettings adcsettings = JAPCSettingsProvider.getAdcSettings(deviceName);
    FESAAcquisitionSettings acquisitionsettings = JAPCSettingsProvider.getAcquisitionSettings(deviceName);
    /* Reflect the new settings in the main panel */
   // mainPanel.setFESASettings(settingswin);
    settingsPanel.setFESASettings(settingspath);
   // expertSettingsPanel.setFESAAdcSettings(adcsettings);
    settingsPanel.setFESAAcquisitionSettings(acquisitionsettings);
  }
  
  public void getData() throws IOException
  {
    /* Get device name from the main panel */
  //  String deviceName = settingsPanel.getDevice();
    String deviceName = this.device;
    if (deviceName == null || deviceName.isEmpty())
    {
      DialogManager.showErrorDialog("Please specify a device first!");
      return;
    }
    /* Get the current dataset and display in the main panel */
    this.data = dataProvider.getData(deviceName);
    dataSet();
   // dataReceived(this.data);
  }
  /**
   * Create the menu bar
   */
  protected void createMenuBar()
  {
    /* Hide the menu bar */
    frame.getJMenuBar().setVisible(false);
    /* Remove any old entries */
    frame.getJMenuBar().removeAll();
    /* Create the file menu */
    JMenu menu = createFileMenu();
    if (menu != null) frame.getJMenuBar().add(menu);
    /* Create the settings menu */
    menu = createSettingsMenu();
    if (menu != null) frame.getJMenuBar().add(menu);
    /* Create the controls menu */
    menu = createControlsMenu();
    if (menu != null) frame.getJMenuBar().add(menu);  
    /* Create the data menu */
    menu = createDataMenu();
    if (menu != null) frame.getJMenuBar().add(menu);
    /* Create the help menu */
    menu = createHelpMenu();
    if (menu != null) frame.getJMenuBar().add(menu);
    /* show the menu bar */
    frame.getJMenuBar().setVisible(true);
  }

  /**
   * Create the file menu
   * @return the menu object
   */
  private JMenu createFileMenu()
  {
    JMenuItem item;
    JMenu menu = new JMenu("File");
    menu.setMnemonic('F');
    item = new JMenuItem(new ControllerAction("Quit","exitApp"));
    item.setAccelerator(KeyStroke.getKeyStroke('Q',InputEvent.CTRL_DOWN_MASK));
    menu.add(item);
    return menu;
  }

  /**
   * Create the settings menu
   * @return the menu object
   */
  private JMenu createSettingsMenu()
  {
    JMenuItem item;
    JMenu menu = new JMenu("Settings");
    menu.setMnemonic('S');
  //  item = new JMenuItem(new ControllerAction("Acquistion Mode","setAcquistionMode"));
	JMenu mnAcquistionMode = new JMenu("Acquistion Mode");
	menu.add(mnAcquistionMode);
	item = new JMenuItem(new ControllerAction("Standard Mode","setStandardAcquistionMode"));
	item.setAccelerator(KeyStroke.getKeyStroke('N',InputEvent.CTRL_DOWN_MASK));
	mnAcquistionMode.add(item);
	item = new JMenuItem(new ControllerAction("Expert Mode","setExpertAcquistionMode"));
	item.setAccelerator(KeyStroke.getKeyStroke('E',InputEvent.CTRL_DOWN_MASK));
	mnAcquistionMode.add(item);
    item = new JMenuItem(new ControllerAction("Set","setSettings"));
    item.setAccelerator(KeyStroke.getKeyStroke('S',InputEvent.CTRL_DOWN_MASK));
    menu.add(item);
    item = new JMenuItem(new ControllerAction("Get","getSettings"));
    item.setAccelerator(KeyStroke.getKeyStroke('G',InputEvent.CTRL_DOWN_MASK));
    menu.add(item);
    return menu;
  }
  
  /**
   * Create the controls menu
   * @return the menu object
   */
  
  private JMenu createControlsMenu()
  {
    JMenuItem item;

    JMenu menu = new JMenu("Controls");
    menu.setMnemonic('C');
    item = new JMenuItem(new ControllerAction("Settings","setSettings"));
    menu.add(item);
    item = new JMenuItem(new ControllerAction("ExpertSettings","setExpertSettings"));
    menu.add(item);
    menu.addSeparator();
    subscribeMenuItem = new JCheckBoxMenuItem(new ControllerAction("Subscribe","subscribe"));
    //    subscribeMenuItem.setAccelerator(KeyStroke.getKeyStroke('C',Event.CTRL_MASK));
    //  subscribeMenuItem.setSelected(dataProvider.isSubscribedData());
    menu.add(subscribeMenuItem);
    measurementMenuItem = new JCheckBoxMenuItem(new ControllerAction("Measure","startMeasurement"));
    //  subscribeMenuItem.setAccelerator(KeyStroke.getKeyStroke('C',Event.CTRL_MASK));
    //  subscribeMenuItem.setSelected(dataProvider.isSubscribedData());
    menu.add(measurementMenuItem);

    return menu;
  }
  /**
   * Create the Data menu
   * @return the menu object
   */
  private JMenu createDataMenu()
  {
    JMenuItem item;
    JMenu menu = new JMenu("Data");
    menu.setMnemonic('D');
    item = new JMenuItem(new ControllerAction("Get Data","getData"));
    item.setAccelerator(KeyStroke.getKeyStroke('D',InputEvent.CTRL_DOWN_MASK));
    menu.add(item);
    return menu;
  }
  /**
   * Create the help menu
   * @return the menu object
   */
  private JMenu createHelpMenu()
  {
    JMenuItem item;

    JMenu menu = new JMenu("Help");
    menu.setMnemonic('H');
    item = new JMenuItem(new ControllerAction("Help","help"));
    menu.add(item);
    menu.addSeparator();
    item = new JMenuItem(new ControllerAction("About","about"));
    menu.add(item);
    return menu;
  }

  /**
   * Find and execute the public method of this class corresponding
   * to a specific action.
   * @param action the method name
   * @param param the parameters of the method
   */
  private void executeAction(String action, Object param)
  {
    try
    {
      if (param == null)
      {
        Method method = getClass().getMethod(action,new Class[0]);
        method.invoke(this,new Object[0]);
      }
      else
      {
        Class<?>[] formparams = new Class[1];
        formparams[0] = Object.class;
        Method method = getClass().getMethod(action,formparams);
        Object[] args = new Object[1];
        args[0] = param;
        method.invoke(this,args);
      }
    }
    catch (Exception e)
    {
      BBQ_GUIApplication.getLogger().error("Action handler method not found or not executed",e);
    }
  }


  /**
   *  This is a helper class implementing an AbstractAction to use with 
   *  menu items, toolbar buttons etc. Besides action name and icon, the
   *  method to execute and its parameters can be specified. 
   */
  @SuppressWarnings("serial")
  protected class ControllerAction extends AbstractAction {

    private String action = null;
    private Object param = null;

    /**
     * Create a default controller action object
     */
    public ControllerAction()
    {
      super();
    }

    /**
     * Create a controller action object with given name, icon, method to
     * execute and parameters
     * @param name the name of the action
     * @param icon the icon for the action
     * @param action the method to execute
     * @param param the parameters for the method to execute
     */
    public ControllerAction(String name, Icon icon, String action, Object param)
    {
      super(name, icon);
      this.action = action;
      this.param = param;
    }

    /**
     * Create a controller action object with given name, icon and 
     * parameterless method to execute.
     * @param name the name of the action
     * @param icon the icon for the action
     * @param action the method to execute
     */
    public ControllerAction(String name, Icon icon, String action)
    {
      this(name,icon,action,null);
    }

    /**
     * Create a controller action object with given name, method to
     * execute and parameters. No icon is set.
     * @param name the name of the action
     * @param action the method to execute
     * @param param the parameters for the method to execute
     */
    public ControllerAction(String name, String action, Object param)
    {
      super(name);
      this.action = action;
      this.param = param;
    }

    /**
     * Create a controller action object with given name, and parameterless
     * method to execute. No icon is set.
     * @param name the name of the action
     * @param action the method to execute
     */
    public ControllerAction(String name, String action)
    {
      this(name,action,null);
    }

    /**
     * Perform the action specified by the action event. It calls
     * the executeAction method of the enclosing class, which determines
     * the method to execute and executes it.
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
      if (action != null) executeAction(action,param);
    }
  }

  private class GUIHandler implements GUIListener {

	    @Override
	    public void handleGUIEvent(GUIEvent e) {
	      switch (e.getCmd())
	      {
	      case BBQGUIEvent.ACTION_SUBSCRIBE:
	        subscribe();
	        BBQ_GUIApplication.getLogger().info("Subscription Start");
	        System.out.println("Controller:: Subscription Start");
	       // unsubscribe();
	        break;
	      case BBQGUIEvent.ACTION_UNSUBSCRIBE:
	    	//  subscribe();
		    unsubscribe();
	    	BBQ_GUIApplication.getLogger().info("Subscription end");
	    	System.out.println("Controller:: Subscription end");
		    break;
	      case BBQGUIEvent.ACTION_SETTINGS_GET:
	   /*     try
	        {
	          settingsProvider.get(bbqSettings);
	        }
	        catch (ParameterException ex)
	        {
	          logger.error("Failed to obtain settings synchronously",ex);
	        }
	        mainPanel.setData(bbqSettings);
	        */
	    	  getSettings();
	        break;
	      case BBQGUIEvent.ACTION_SETTINGS_SET:
	   /*     if (bbqSettings == null)
	        	bbqSettings = new BBQSettings();
	        mainPanel.get(bbqSettings);
	        setSettings(bbqSettings);
	        break;
	      case BBQGUIEvent.ACTION_EXPERTSETTINGS_GET:
	        try
	        {
	          expertSettingsProvider.get(bbqExpertSettings);
	        }
	        catch (ParameterException ex)
	        {
	          logger.error("Failed to obtain expert settings synchronously",ex);
	        }
	        mainPanel.setData(bbqExpertSettings);*/
	    	  setSettings();
	        break;
	      case BBQGUIEvent.ACTION_EXPERTSETTINGS_SET:
	    	  /*
	        if (bbqExpertSettings == null) bbqExpertSettings = new BBQExpertSettings();
	        mainPanel.get(bbqExpertSettings);
	        setSettings(bbqExpertSettings);
	        */
	    	  
	        break;
	      case BBQGUIEvent.ACTION_MEASUREMENT:
	        startMeasurement();
	        break;
	   //   case BBQGUIEvent.ACTION_ANALYSIS_STATISTICS:
	   //     if (data != null) mainPanel.setData(data.getStatistics());
	   //     break;
	      case BBQGUIEvent.ACTION_DISPLAY_GRAPH:
	        displayGraph(e.getData().toString());
	        break;
	      case BBQGUIEvent.ACTION_DISPLAY_LAYOUT:
	        layoutGraph(e);
	        break;
	      case BBQGUIEvent.ACTION_DISPLAY_CLEAR:
	        clearGraph();
	        break;
	      case BBQGUIEvent.ACTION_DISPLAY_SYNC:
	        synchronizeAxis((Boolean)e.getData());
	        break;
	      case BBQGUIEvent.ACTION_DISPLAY_FOCUS:
	      case BBQGUIEvent.ACTION_DISPLAY_RANGE:
	        mainPanel.setData(e.getData());
	        break;
	      case BBQGUIEvent.ACTION_DISPLAY_SETXRANGE:
	        setXRange((DataRange)e.getData());
	        break;
	      case BBQGUIEvent.ACTION_DISPLAY_SETYRANGE:
	        setYRange((DataRange)e.getData());
	        break;
	      case BBQGUIEvent.ACTION_DISPLAY_AUTOXRANGE:
	        setXAutoRange((Boolean)e.getData());
	        break;
	      case BBQGUIEvent.ACTION_DISPLAY_AUTOYRANGE:
	        setYAutoRange((Boolean)e.getData());
	        break;
	//      case BBQGUIEvent.ACTION_FILTER_ADD:
	//        addFilter();
	//        break;
	//      case BBQGUIEvent.ACTION_FILTER_REMOVE:
	//        removeFilter((Integer)e.getData());
	//        break;
	//      case BBQGUIEvent.ACTION_FILTER_EDIT:
	//        editFilter((Integer)e.getData());
	//        break;
	      }
	    }

	  }

public void handleProviderEvent(ProviderEvent event) 
{
  if (event.getType() == ProviderEvent.TYPE_STATUS)
  {
    setStatus(event.getData());
  }
  else if (event.getType() == ProviderEvent.TYPE_GENERIC)
  {
    System.out.println("handleProviderEvent: "+event.getData());
    mainPanel.setData(event.getData());
    if (event.getData() instanceof BBQData)
    {
      try {
		setData((BBQData)event.getData());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
    else if (event.getData() instanceof BBQSettings)
    {
      updateSettings((BBQSettings)event.getData());
    }
    else if (event.getData() instanceof BBQExpertSettings)
    {
      updateExpertSettings((BBQExpertSettings)event.getData());
    }
  }
  else if (event.getType() == ProviderEvent.TYPE_DISCONNECTED)
  {
    try {
		handleDisconnect();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  else if (event.getType() == ProviderEvent.TYPE_RECONNECTED)
  {
    handleConnect();
  }
//  else if (event.getType() == AbstractProvider.PROVIDER_EVENT_TYPE_EXTERNAL_CHANGE)
//  {
//    ExternalChangeDialog.getInstance().show(frame.getJFrame());
//    //      DialogManager.showWarningDialog("FESA class settings have been modified from outside this application");
//  }
}

private void handleConnect()
{
  controlProvider.powerOn();
  try
  {
    settingsProvider.get(bbqSettings);
    expertSettingsProvider.get(bbqExpertSettings);
  }
  catch (ParameterException e)
  {
    logger.error("Settings provider get failed",e);
  }
}

private void handleDisconnect() throws IOException
{
  setData(new BBQData());
}


private void displayGraph(String name)
{
  GraphPanel graphPanel = (GraphPanel)mainPanel.findPanel(GraphPanel.class);
  graphPanel.addGraph(data,name);
}

private void layoutGraph(GUIEvent e)
{
  int[] a = (int[])e.getData();
  GraphPanel graphPanel = (GraphPanel)mainPanel.findPanel(GraphPanel.class);
  graphPanel.layout(a[0],a[1]);
}

private void clearGraph()
{
  GraphPanel graphPanel = (GraphPanel)mainPanel.findPanel(GraphPanel.class);
  graphPanel.clearGraph();
}

private void synchronizeAxis(boolean sync)
{
  GraphPanel graphPanel = (GraphPanel)mainPanel.findPanel(GraphPanel.class);
  graphPanel.synchronizeAxis(sync);
}

private void setXRange(DataRange range)
{
  GraphPanel graphPanel = (GraphPanel)mainPanel.findPanel(GraphPanel.class);
  graphPanel.setXRange(range);
}

private void setYRange(DataRange range)
{
  GraphPanel graphPanel = (GraphPanel)mainPanel.findPanel(GraphPanel.class);
  graphPanel.setYRange(range);
}

private void setXAutoRange(boolean b)
{
  GraphPanel graphPanel = (GraphPanel)mainPanel.findPanel(GraphPanel.class);
  graphPanel.setXAutoRange(b);
}

private void setYAutoRange(boolean b)
{
  GraphPanel graphPanel = (GraphPanel)mainPanel.findPanel(GraphPanel.class);
  graphPanel.setYAutoRange(b);
}

public void subscribe()
{
  if (device == null || device.equals(""))
  {
  }
  else
  {
    if (dataProvider.isSubscribedData())
    {
      dataProvider.unsubscribe();
    }
    else
    {
      dataProvider.subscribe();
      dataProvider.subscribe(this.device);
    }
  }
 // mainPanel.setSubscribed(dataProvider.isSubscribed());
 // subscribeMenuItem.setSelected(dataProvider.isSubscribed());
}

public void unsubscribe()
{
  if (device == null || device.equals(""))
  {
	  
  }
  else
  {
    if (dataProvider.isSubscribedData())
    {
      dataProvider.unsubscribe();
    }
  }
 // mainPanel.setSubscribed(dataProvider.isSubscribed());
 // subscribeMenuItem.setSelected(dataProvider.isSubscribed());
}

/*public void setSettings()  // This will be the new way of setting
{
  try
  {
    settingsProvider.get(bbqSettings);
  }
  catch (ParameterException e)
  {
    logger.error("Failed to obtain settings synchronously",e);
  }
  SettingsDialog d = new SettingsDialog(frame.getJFrame(),bbqSettings);
  d.setVisible(true);
  if (d.isAccepted())
  {
    d.getSettings(bbqSettings);
    setSettings(bbqSettings);
  }
}*/

public void setExpertSettings()
{
  try
  {
    expertSettingsProvider.get(bbqExpertSettings);
  }
  catch (ParameterException e)
  {
    logger.error("Failed to obtain expert settings synchronously",e);
  }
  ExpertSettingsDialog d = new ExpertSettingsDialog(frame.getJFrame(),bbqExpertSettings);
  d.setVisible(true);
  if (d.isAccepted())
  {
    d.getSettings(bbqExpertSettings);
    setSettings(bbqExpertSettings);
  }
}

public void startMeasurement()
{
  BBQStatus status = new BBQStatus();
  try
  {
    statusProvider.get(status);
  }
  catch (ParameterException e)
  {
  }
  if (status.isStopped())
  {
    BBQSettings s = new BBQSettings();
    mainPanel.get(s);
    BBQExpertSettings es = new BBQExpertSettings();
    mainPanel.get(es);
    if ((!es.equals(bbqExpertSettings)) || (!s.equals(bbqSettings)))
    {
      int ret = DialogManager.showConfirmDialog("Send modified settings to FESA class?");
      if (ret == DialogManager.YES_OPTION)
      {
        try
        {
          if (!s.equals(bbqSettings))
          {
            settingsProvider.set(s);
            expectSettings = true;
          }
          if (!es.equals(bbqExpertSettings))
          {
            expertSettingsProvider.set(es);
            expectExpertSettings = true;
          }
        } 
        catch (ParameterException e) 
        {
          logger.error("Failed to send settings to device "+device,e);
          DialogManager.showErrorDialog("Failed to send settings to device "+device+"!\nReason: "+e.getMessage(),e);
        }
      }
    }
    controlProvider.startMeasurement();
  }
  else
  {
    controlProvider.stopMeasurement();
  }
  try
  {
    statusProvider.get(status);
  }
  catch (ParameterException e)
  {
  }
  mainPanel.setStatus(status);
  measurementMenuItem.setSelected(!status.isStopped());
}

private void setSettings(BBQSettings settings)
{
  int sampleLength = (int)(settings.getSampleTime() / 1000.0 * (bbqExpertSettings.getSamplingFrequency()*1e6));
  if (sampleLength > JAPCDataProvider.MAX_SAMPLELENGTH)
  {
    int reduction = (sampleLength + JAPCDataProvider.MAX_SAMPLELENGTH - 1) / JAPCDataProvider.MAX_SAMPLELENGTH;
    DataProviderReductionDialog d = new DataProviderReductionDialog(frame.getJFrame(),reduction);
    d.setVisible(true);
    if (!d.isAccepted())
    {
      return;
    }
   // dataProvider.setReduction(d.getReduction());
  }
/*  else if (dataProvider.getReduction() != JAPCDataProvider.NO_REDUCTION)
  {
    dataProvider.setReduction(DataProvider.NO_REDUCTION);
    DialogManager.showInfoDialog("Full FCT data is now obtained from FESA class.");
  }
  guiStatus.setFecDataReduction(dataProvider.getReduction());
  mainPanel.setStatus(guiStatus);*/
  try
  {
    settingsProvider.set(settings);
    expectSettings = true;
  } 
  catch (ParameterException e) 
  {
    logger.error("Failed to send settings to device "+device,e);
    DialogManager.showErrorDialog("Failed to send settings to device "+device+"!\nReason: "+e.getMessage(),e);
  }
}

private void setSettings(BBQExpertSettings settings)
{
  int sampleLength = (int)(bbqSettings.getSampleTime() / 1000.0 * (settings.getSamplingFrequency()*1e6));
  if (sampleLength > JAPCDataProvider.MAX_SAMPLELENGTH)
  {
    int reduction = (sampleLength + JAPCDataProvider.MAX_SAMPLELENGTH - 1) / JAPCDataProvider.MAX_SAMPLELENGTH;
    DataProviderReductionDialog d = new DataProviderReductionDialog(frame.getJFrame(),reduction);
    d.setVisible(true);
    if (!d.isAccepted())
    {
      return;
    }
 //   dataProvider.setReduction(d.getReduction());
  }
 /* else if (dataProvider.getReduction() != DataProvider.NO_REDUCTION)
  {
    dataProvider.setReduction(DataProvider.NO_REDUCTION);
    DialogManager.showInfoDialog("Full FCT data is now obtained from FESA class.");
  }
  guiStatus.setFecDataReduction(dataProvider.getReduction());
  mainPanel.setStatus(guiStatus);*/
  try
  {
    expertSettingsProvider.set(settings);
    expectExpertSettings = true;
  } 
  catch (ParameterException e) 
  {
    logger.error("Failed to send expert settings to device "+device,e);
    DialogManager.showErrorDialog("Failed to send expert settings to device "+device+"!\nReason: "+e.getMessage(),e);
  }
}


private void updateSettings(BBQSettings settings)
{
  bbqSettings = settings;
  if (!expectSettings)
  {
    ExternalChangeDialog.getInstance().show(frame.getJFrame());
  }
  expectSettings = false;
}

private void updateExpertSettings(BBQExpertSettings settings)
{
  bbqExpertSettings = settings;
  if (!expectExpertSettings)
  {
    ExternalChangeDialog.getInstance().show(frame.getJFrame());
  }
  expectExpertSettings = false;
}

private void setStatus(Object status) 
{
  mainPanel.setStatus(status);
  if (status != null && status instanceof BBQStatus)
  {
    measurementMenuItem.setSelected(!((BBQStatus)status).isStopped());
  }
}

private void setData(BBQData data) throws IOException
{
	BBQ_GUIApplication.getLogger().info("Controller :: Receiving data in controller");
  if (data != null)
	 {
  this.data = data;
  dataSet();
	 }
  if (operDBData != null && data != null)
  {
    mainPanel.setData(operDBData.getInfo(data.getCycleId()));
  }
}

/**public void dataReceived(BBQData data)
{
  BBQ_GUIApplication.getLogger().info("Controller:: Recieve and Set FESA Data");
  settingsPanel.setFESAData(data, dataPanel);
}
 * @throws IOException 
**/

public void dataSet() throws IOException
{
  BBQ_GUIApplication.getLogger().info("Controller:: Setting FESA Data");
  settingsPanel.setFESAData(data,dataPanel);
}

private class OperDBHandler implements OPERDBListener {
  
  @Override
  public void handleOPERDBEvent(OPERDBEvent event)
  {
    operDBData = event.getData();
  }

}


}

