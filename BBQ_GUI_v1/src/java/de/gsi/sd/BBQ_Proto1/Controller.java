/*****************************************************************************
 *                                                                           *
 * PrimerExample1 - Application controller                                   *
 *                                                                           *
 * modified: 2010-08-04 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.lang.reflect.Method;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import cern.accsoft.gui.beans.HtmlReaderPanel;
import cern.accsoft.gui.frame.DialogManager;
import cern.accsoft.gui.frame.ExternalFrame;
import cern.accsoft.gui.frame.FrameManager;
import de.gsi.sd.BBQ_Proto1.data.FESAAcquisitionSettings;
import de.gsi.sd.BBQ_Proto1.data.FESAData;
import de.gsi.sd.BBQ_Proto1.data.FESASettingsPath;
import de.gsi.sd.BBQ_Proto1.data.FESAAdcSettings;
import de.gsi.sd.BBQ_Proto1.data.FESASettingsWindow;
import de.gsi.sd.BBQ_Proto1.data.provider.JAPCDataProvider;
import de.gsi.sd.BBQ_Proto1.data.provider.JAPCDataProviderListener;
import de.gsi.sd.BBQ_Proto1.data.provider.JAPCSettingsProvider;
import de.gsi.sd.BBQ_Proto1.dialogs.AboutDialog;
import de.gsi.sd.BBQ_Proto1.gui.MainPanel;
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
public class Controller implements ActionListener, JAPCDataProviderListener {

  private ExternalFrame frame;
  private MainPanel mainPanel;
  private JAPCDataProvider dataProvider;
  
  /**
   * Constructor for the Controller object. 
   * @param frame the application main frame
   * @param mainPanel the GUI main panel
   */
  public Controller(ExternalFrame frame, MainPanel mainPanel)
  {
    this.frame = frame;
    this.mainPanel = mainPanel;
    /* Add this class as action listener to the main panel (for the subscribe button) */
    mainPanel.addActionListener(this);
    /* Create the menu bar */
    createMenuBar();
    /* Create the data provider and register as a listener */
    dataProvider = new JAPCDataProvider();
    dataProvider.addDataProviderListener(this);
  }

  /**
   * Perform an action specified by the action event
   * @param e the action event
   */
  @Override
  public void actionPerformed(ActionEvent e) 
  {
    if (e.getActionCommand().equals(MainPanel.ACTION_SUBSCRIBE))
      dataProvider.subscribe(mainPanel.getDevice());
    else if (e.getActionCommand().equals(MainPanel.ACTION_UNSUBSCRIBE))
      dataProvider.unsubscribe();
  }
  
  /**
   * Handle the reception of data from the data provider.
   * @param data the FESA data received
   */
  @Override
  public void dataReceived(FESAData data)
  {
    mainPanel.setFESAData(data);
  }
  
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
    String deviceName = mainPanel.getDevice();
    if (deviceName == null || deviceName.isEmpty())
    {
      DialogManager.showErrorDialog("Please specify a device first!");
      return;
    }
    /* Get the FESA settings from the main panel */
    mainPanel.setStandardAcquisitionMode();
  }
  
  public void setExpertAcquistionMode()
  {
    /* Get device name from the main panel */
    String deviceName = mainPanel.getDevice();
    if (deviceName == null || deviceName.isEmpty())
    {
      DialogManager.showErrorDialog("Please specify a device first!");
      return;
    }
    /* Get the FESA settings from the main panel */
    mainPanel.setExpertAcquisitionMode();
  }
  /**
   * Set the Setting property of the FESA device
   */
  public void setSettings()
  {
    /* Get device name from the main panel */
    String deviceName = mainPanel.getDevice();
    if (deviceName == null || deviceName.isEmpty())
    {
      DialogManager.showErrorDialog("Please specify a device first!");
      return;
    }
    /* Get the FESA settings from the main panel */
    FESASettingsWindow settingswin = mainPanel.getFESASettingsWindow();
    FESASettingsPath settingspath = mainPanel.getFESASettingsPath();
    FESAAdcSettings adcsettings = mainPanel.getFESAAdcSettings();
    FESAAcquisitionSettings acquisitionsettings = mainPanel.getFESAAcquisitionSettings();
    /* Set the settings via the JAPCSettingsProvider */
 //   JAPCSettingsProvider.setWindow(deviceName,settingswin);
    JAPCSettingsProvider.setPath(deviceName,settingspath);
    JAPCSettingsProvider.setAdcSettings(deviceName,adcsettings);
    JAPCSettingsProvider.setAcquisitionSettings(deviceName,acquisitionsettings);
  }
  
  /**
   * Get the FESA settings from the device
   */
  public void getSettings()
  {
    /* Get device name from the main panel */
    String deviceName = mainPanel.getDevice();
    if (deviceName == null || deviceName.isEmpty())
    {
      DialogManager.showErrorDialog("Please specify a device first!");
      return;
    }
    /* Get the FESA settings from the device via the JAPCSettingsProvider */
  //  FESASettingsWindow settingswin = JAPCSettingsProvider.getWindow(deviceName);
    FESASettingsPath settingspath = JAPCSettingsProvider.getPath(deviceName);
    FESAAdcSettings adcsettings = JAPCSettingsProvider.getAdcSettings(deviceName);
    FESAAcquisitionSettings acquisitionsettings = JAPCSettingsProvider.getAcquisitionSettings(deviceName);
    /* Reflect the new settings in the main panel */
   // mainPanel.setFESASettings(settingswin);
    mainPanel.setFESASettings(settingspath);
    mainPanel.setFESAAdcSettings(adcsettings);
    mainPanel.setFESAAcquisitionSettings(acquisitionsettings);
  }
  
  public void getData()
  {
    /* Get device name from the main panel */
    String deviceName = mainPanel.getDevice();
    if (deviceName == null || deviceName.isEmpty())
    {
      DialogManager.showErrorDialog("Please specify a device first!");
      return;
    }
    /* Get the current dataset and display in the main panel */
    dataProvider.getData(deviceName);
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

}
