/*****************************************************************************
 *                                                                           *
 * PrimerExample1 - JAPC Settings Provider                                   *
 *                                                                           *
 * modified: 2010-08-04 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.data.provider;

import cern.japc.AcquiredParameterValue;
import cern.japc.ImmutableMapParameterValue;
import cern.japc.MapParameterValue;
import cern.japc.Parameter;
import cern.japc.ParameterValue;
import cern.japc.Selector;
import cern.japc.Type;
import cern.japc.factory.ParameterFactory;
import cern.japc.factory.ParameterValueFactory;
import cern.japc.spi.ParameterUrl;
import cern.japc.spi.ParameterUrlImpl;
import de.gsi.sd.BBQ_Proto1.BBQ_GUIApplication;
import de.gsi.sd.BBQ_Proto1.data.FESAAcquisitionSettings;
import de.gsi.sd.BBQ_Proto1.data.FESAAdcSettings;
import de.gsi.sd.BBQ_Proto1.data.FESASettingsWindow;
import de.gsi.sd.BBQ_Proto1.data.FESASettingsPath;


/**
 * This class connects to the FESA device and sets or gets the values
 * of the Setting property. Because all access is done synchronously,
 * all methods are realized as static methods. A more refined GUI may
 * also subscribe to the Setting property to be automatically notified
 * when another client changes the settings. 
 */
public class JAPCSettingsProvider {

  /** The instance of the JAPC parameter factors */
  static private ParameterFactory japcParameterFactory = ParameterFactory.newInstance();

  /** Name of the property accessed */
 // static private final String PROPERTY_SETTINGS = "DataWindowSetting";
  /** Name of the window field */
 //  static private final String FIELD_WINDOW  = "dataWindowLengthClient";
  
  static private final String PROPERTY_SETTINGS1 = "SummaryIOSetting";
  /** Name of the path field */
  static private final String FIELD_PATH  = "fileBase"; 
  
  static private final String PROPERTY_ADCSETTINGS = "AdcSettings";
  /** Name of the mode field */
  static private final String FIELD_ADC_MODE  = "acqMode";
  static private final String FIELD_ADCSETTINGS  = "adcSettings"; 
  
  static private final String PROPERTY_ACQUISITIONSETTINGS = "AcquisitionSettings";
  /** Name of the acquisition settings field */
  static private final String FIELD_MODE  = "operationMode"; 
  /** Name of the adc settings field */
  static private final String FIELD_ACQUISITIONSETTINGS  = "acquistionSettings";
  /**
   * Get the FESA settings from the device.
   * @param deviceName the name of the FESA device
   * @return the FESA settings object or null in case of error
   */
  /*static public FESASettingsWindow getWindow(String deviceName) 
  {
     Check for device name 
    if (deviceName == null || deviceName.isEmpty()) 
    {
      BBQ_GUIApplication.getLogger().error("No device specfied!");
      return null;
    }
     Get a URL for a parameter getting the setting property 
    ParameterUrl url = new ParameterUrlImpl(deviceName,PROPERTY_SETTINGS);
     Get a cycle selector for not multiplexed device 
    Selector selector = ParameterValueFactory.newSelector(null);
    try 
    {
       Get a parameter for the acquisition property 
      Parameter p = japcParameterFactory.newParameter(url);
       Get the settings from the FESA class 
      AcquiredParameterValue acquiredValue = p.getValue(selector);
       Get the actual parameter value 
      ParameterValue value = acquiredValue.getValue(); 
       If the property has more than one field the parameter value will be of
       * type MAP 
      if (value.getType().equals(Type.MAP))
      {
         Create new FESA settings object  
        FESASettingsWindow s = new FESASettingsWindow();
         Cast the parameter value to its correct class of type MAP 
        ImmutableMapParameterValue map = (ImmutableMapParameterValue)value;
         Obtain the value of the field given by FIELD_OFFSET (i.e. "offset") as
         * a double value and set it in the FESA settings object 
        s.setOffset(map.getShort(FIELD_WINDOW));
         Obtain the value of the field given by FIELD_RANGE (i.e. "range") as
         * a double value and set it in the FESA settings object 
       *//** s.setRange(( map.getString("DataPathSetting")));*//*
        return s;
      }
    } 
    
    catch (Exception e) 
    {
    	BBQ_GUIApplication.getLogger().error("Failed to get data from device "+deviceName,e);
    }
     Error or unhandled parameter type: return null 
    

    return null;
  }*/
  static public FESASettingsPath getPath(String deviceName) 
  {
    /* Check for device name */
    if (deviceName == null || deviceName.isEmpty()) 
    {
      BBQ_GUIApplication.getLogger().error("No device specfied!");
      return null;
    }
    /* Get a URL for a parameter getting the setting property */
    ParameterUrl url = new ParameterUrlImpl(deviceName,PROPERTY_SETTINGS1);
    /* Get a cycle selector for not multiplexed device */
    Selector selector = ParameterValueFactory.newSelector(null);
    try 
    {
      /* Get a parameter for the acquisition property */
      Parameter p = japcParameterFactory.newParameter(url);
      /* Get the settings from the FESA class */
      AcquiredParameterValue acquiredValue = p.getValue(selector);
      /* Get the actual parameter value */
      ParameterValue value = acquiredValue.getValue(); 
      /* If the property has more than one field the parameter value will be of
       * type MAP */
      if (value.getType().equals(Type.MAP))
      {
        /* Create new FESA settings object */ 
        FESASettingsPath s = new FESASettingsPath();
        /* Cast the parameter value to its correct class of type MAP */
        ImmutableMapParameterValue map = (ImmutableMapParameterValue)value;
        /* Obtain the value of the field given by FIELD_OFFSET (i.e. "offset") as
         * a double value and set it in the FESA settings object */
        s.setRange(map.getString(FIELD_PATH));
        /* Obtain the value of the field given by FIELD_RANGE (i.e. "range") as
         * a double value and set it in the FESA settings object */
       /** s.setRange(( map.getString("DataPathSetting")));*/
        return s;
      }
    } 
    catch (Exception e) 
    {
    	BBQ_GUIApplication.getLogger().error("Failed to get data from device "+deviceName,e);
    }
    /* Error or unhandled parameter type: return null */
    

    return null;
  }

  static public FESAAdcSettings getAdcSettings(String deviceName) 
  {
    /* Check for device name */
    if (deviceName == null || deviceName.isEmpty()) 
    {
      BBQ_GUIApplication.getLogger().error("No device specfied!");
      return null;
    }
    /* Get a URL for a parameter getting the setting property */
    ParameterUrl url = new ParameterUrlImpl(deviceName,PROPERTY_ADCSETTINGS);
    /* Get a cycle selector for not multiplexed device */
    Selector selector = ParameterValueFactory.newSelector(null);
    try 
    {
      /* Get a parameter for the acquisition property */
      Parameter p = japcParameterFactory.newParameter(url);
      /* Get the settings from the FESA class */
      AcquiredParameterValue acquiredValue = p.getValue(selector);
      /* Get the actual parameter value */
      ParameterValue value = acquiredValue.getValue(); 
      /* If the property has more than one field the parameter value will be of
       * type MAP */
      if (value.getType().equals(Type.MAP))
      {
        /* Create new FESA settings object */ 
        FESAAdcSettings s = new FESAAdcSettings();
        /* Cast the parameter value to its correct class of type MAP */
        ImmutableMapParameterValue map = (ImmutableMapParameterValue)value;
        /* Obtain the value of the field given by FIELD_OFFSET (i.e. "offset") as
         * a double value and set it in the FESA settings object */
        s.setMode(map.getByte(FIELD_MODE));
        s.setAdcSettings(map.getShorts(FIELD_ADCSETTINGS));
  
        return s;
      }
    } 
    catch (Exception e) 
    {
    	BBQ_GUIApplication.getLogger().error("Failed to get data from device "+deviceName,e);
    }
    /* Error or unhandled parameter type: return null */
    

    return null;
  }
  static public FESAAcquisitionSettings getAcquisitionSettings(String deviceName) 
  {
    /* Check for device name */
    if (deviceName == null || deviceName.isEmpty()) 
    {
      BBQ_GUIApplication.getLogger().error("No device specfied!");
      return null;
    }
    /* Get a URL for a parameter getting the setting property */
    ParameterUrl url = new ParameterUrlImpl(deviceName,PROPERTY_ACQUISITIONSETTINGS);
    /* Get a cycle selector for not multiplexed device */
    Selector selector = ParameterValueFactory.newSelector(null);
    try 
    {
      /* Get a parameter for the acquisition property */
      Parameter p = japcParameterFactory.newParameter(url);
      /* Get the settings from the FESA class */
      AcquiredParameterValue acquiredValue = p.getValue(selector);
      /* Get the actual parameter value */
      ParameterValue value = acquiredValue.getValue(); 
      /* If the property has more than one field the parameter value will be of
       * type MAP */
      if (value.getType().equals(Type.MAP))
      {
        /* Create new FESA settings object */ 
        FESAAcquisitionSettings s = new FESAAcquisitionSettings();
        /* Cast the parameter value to its correct class of type MAP */
        ImmutableMapParameterValue map = (ImmutableMapParameterValue)value;
        /* Obtain the value of the field given by FIELD_OFFSET (i.e. "offset") as
         * a double value and set it in the FESA settings object */
        s.setMode(map.getByte(FIELD_ADC_MODE));
        s.setAcquisitionSettings(map.getShorts(FIELD_ACQUISITIONSETTINGS));
  
        return s;
      }
    } 
    catch (Exception e) 
    {
    	BBQ_GUIApplication.getLogger().error("Failed to get data from device "+deviceName,e);
    }
    /* Error or unhandled parameter type: return null */
    

    return null;
  }
  
  /**
   * Set the settings of the FESA device
   * @param deviceName the name of the FESA device
   * @param s the FESA settings to set
   */
/*  static public void setWindow(String deviceName, FESASettingsWindow s)
  {
     Check for device name 
    if (deviceName == null || deviceName.isEmpty()) 
    {
    	BBQ_GUIApplication.getLogger().error("No device specfied!");
      return;
    }
     Get a URL for a parameter setting the setting property 
    ParameterUrl url = new ParameterUrlImpl(deviceName,PROPERTY_SETTINGS);
     Get a cycle selector for not multiplexed device 
    Selector selector = ParameterValueFactory.newSelector(null);
    try
    {
       Get a parameter for the acquisition property 
      Parameter p = japcParameterFactory.newParameter(url);
       Create a new parameter value of type MAP 
      MapParameterValue map = ParameterValueFactory.newParameterValue();
       Set the value of the field FIELD_OFFSET 
      map.setShort(FIELD_WINDOW,s.getOffset());
       Set the value of the field FIELD_RANGE 
     *//**  map.setString("DataPathSetting",s.getRange());*//*
       Set the parameter value in the parameter (i.e. send the data to the
       * FESA device) 
      p.setValue(selector,map);
    } 
    catch (Exception e) 
    {
    	BBQ_GUIApplication.getLogger().error("Failed to send settings to device "+deviceName,e);
    }
  }*/

  static public void setPath(String deviceName, FESASettingsPath s)
  {
    /* Check for device name */
    if (deviceName == null || deviceName.isEmpty()) 
    {
    	BBQ_GUIApplication.getLogger().error("No device specfied!");
      return;
    }
    /* Get a URL for a parameter setting the setting property */
    ParameterUrl url = new ParameterUrlImpl(deviceName,PROPERTY_SETTINGS1);
    /* Get a cycle selector for not multiplexed device */
    Selector selector = ParameterValueFactory.newSelector(null);
    try
    {
      /* Get a parameter for the acquisition property */
      Parameter p = japcParameterFactory.newParameter(url);
      /* Create a new parameter value of type MAP */
      MapParameterValue map = ParameterValueFactory.newParameterValue();
      /* Set the value of the field FIELD_OFFSET */
      map.setString(FIELD_PATH,s.getRange());
      /* Set the value of the field FIELD_RANGE */
     /**  map.setString("DataPathSetting",s.getRange());*/
      /* Set the parameter value in the parameter (i.e. send the data to the
       * FESA device) */
      p.setValue(selector,map);
    } 
    catch (Exception e) 
    {
    	BBQ_GUIApplication.getLogger().error("Failed to send settings to device "+deviceName,e);
    }
  }
  
  static public void setAdcSettings(String deviceName, FESAAdcSettings s)
  {
    /* Check for device name */
    if (deviceName == null || deviceName.isEmpty()) 
    {
    	BBQ_GUIApplication.getLogger().error("No device specfied!");
      return;
    }
    /* Get a URL for a parameter setting the setting property */
    ParameterUrl url = new ParameterUrlImpl(deviceName,PROPERTY_ADCSETTINGS);
    /* Get a cycle selector for not multiplexed device */
    Selector selector = ParameterValueFactory.newSelector(null);
    try
    {
      /* Get a parameter for the acquisition property */
      Parameter p = japcParameterFactory.newParameter(url);
      /* Create a new parameter value of type MAP */
      MapParameterValue map = ParameterValueFactory.newParameterValue();
      /* Set the value of the field FIELD_OFFSET */
      map.setByte(FIELD_ADC_MODE,s.getMode());
      map.setShorts(FIELD_ADCSETTINGS,s.getAdcSettings());
      /* Set the value of the field FIELD_RANGE */
     /**  map.setString("DataPathSetting",s.getRange());*/
      /* Set the parameter value in the parameter (i.e. send the data to the
       * FESA device) */
      p.setValue(selector,map);
    } 
    catch (Exception e) 
    {
    	BBQ_GUIApplication.getLogger().error("Failed to send settings to device "+deviceName,e);
    }
  }
  static public void setAcquisitionSettings(String deviceName, FESAAcquisitionSettings s)
  {
    /* Check for device name */
    if (deviceName == null || deviceName.isEmpty()) 
    {
    	BBQ_GUIApplication.getLogger().error("No device specfied!");
      return;
    }
    /* Get a URL for a parameter setting the setting property */
    ParameterUrl url = new ParameterUrlImpl(deviceName,PROPERTY_ACQUISITIONSETTINGS);
    /* Get a cycle selector for not multiplexed device */
    Selector selector = ParameterValueFactory.newSelector(null);
    try
    {
      /* Get a parameter for the acquisition property */
      Parameter p = japcParameterFactory.newParameter(url);
      /* Create a new parameter value of type MAP */
      MapParameterValue map = ParameterValueFactory.newParameterValue();
      /* Set the value of the field FIELD_OFFSET */
      map.setByte(FIELD_MODE,s.getMode());
      map.setShorts(FIELD_ACQUISITIONSETTINGS,s.getAcquisitionSettings());
      /* Set the value of the field FIELD_RANGE */
     /**  map.setString("DataPathSetting",s.getRange());*/
      /* Set the parameter value in the parameter (i.e. send the data to the
       * FESA device) */
      p.setValue(selector,map);
    } 
    catch (Exception e) 
    {
    	BBQ_GUIApplication.getLogger().error("Failed to send settings to device "+deviceName,e);
    }
  }
}
