/*****************************************************************************
 *                                                                           *
 * BBQ GUI - Application                                                     *
 *                                                                           *
 * modified: 2014-05-21 Rahul Singh                                          *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import de.gsi.sd.BBQ_Proto1.gui.MainPanel;
import de.gsi.sd.common.language.Language;
import de.gsi.sd.BBQ_Proto1.BBQ_GUIApplication;
import org.apache.log4j.Logger;
import cern.accsoft.gui.frame.ExternalFrame;
import cern.accsoft.gui.frame.FrameManager;
/**
 * This class is the main application class. It is usually quite short and just
 * initializes all components of the application. It also contains the main
 * method. 
 */
public class BBQ_GUIApplication {

  /** Public logger object for the whole application */
  static public Logger logger = Logger.getLogger(BBQ_GUIApplication.class);
  
  /**
   * The application object constructor initializes all components.
   */
  public BBQ_GUIApplication()
  {
    /* First load all properties */
    loadProperties();
 //   Language.getInstance().addResource(BBQ_GUIApplication.class,"de.gsi.sd.BBQ_Proto1.language.BBQ_Proto1");
    /* Get an instance of the frame manager */
    FrameManager manager = FrameManager.getInstance();
    /* Create the main panel */
    MainPanel mainPanel = new MainPanel();
    /* Get the main application frame with the title "PrimerExample1" and
     * the just created main panel. */
    ExternalFrame frame = manager.getMainFrame("BBQ GUI",mainPanel);
    /* Create the application controller */
    new Controller(frame,mainPanel);
    /* Hide the logging console of the main frame */
    frame.setConsoleVisible(false);
    /* Show the application main fame */
    frame.setVisible(true);
  }
  
  /**
   * Static method to obtain the application wide logger object
   * @return the logger object
   */
  static public Logger getLogger()
  {
    return logger;
  }
  
  /**
   * Load important properties used for all SD applications. These properties
   * contain for example the list directory name servers, the location
   * of the JAPC service configurations, the SD help server name etc.
   * The properties are provided via a web server. 
   */
  private void loadProperties()
  {
    try
    {
      /*
       * Get the location of the SD properties file. The default can be 
       * overwritten by specifying -DSDConfigSrv=... as a startup parameter
       * of the application;
       */
      String configSrv = System.getProperty("SDConfigSrv","http://sdlx014.acc.gsi.de/config/SD.properties");
      URL url = new URL(configSrv);
      /* Load the properties from the URL */
      InputStream input = url.openStream();
      Properties p = new Properties();
      p.load(input);
      /*
       * Copy the loaded properties to the system properties. However, copy
       * only if the property does not exist in the system properties. Thus
       * system properties are not overwritten. As system properties can be set
       * as startup parameters of the application, this allows to overwrite
       * properties from the properties file.
       */
      Set<String> keys = p.stringPropertyNames();
      for (String key : keys)
      {
        if (System.getProperty(key) == null)
        {
          System.setProperty(key,p.getProperty(key));
        }
      }
    }
    catch (Exception e)
    {
      /*
       * The properties could not be loaded. Print a stack trace for debugging
       * purposes and do not worry. 
       */
      e.printStackTrace();
    }
  }
  

  /**
   * The main entry point for the application.
   * @param args array of command line arguments (not used)
   */
  public static void main(String[] args) 
  {
    new BBQ_GUIApplication();
  }

}
