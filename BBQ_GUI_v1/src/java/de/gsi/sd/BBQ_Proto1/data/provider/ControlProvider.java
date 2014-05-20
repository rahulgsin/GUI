package de.gsi.sd.BBQ_Proto1.data.provider;

import cern.accsoft.gui.frame.DialogManager;
import cern.japc.MapParameterValue;
import cern.japc.Parameter;
import cern.japc.Selector;
import cern.japc.factory.ParameterFactory;
import cern.japc.factory.ParameterValueFactory;
import cern.japc.spi.ParameterUrl;
import cern.japc.spi.ParameterUrlImpl;
import org.apache.log4j.Logger;


public class ControlProvider {
	  private String device = null;
	  private ParameterFactory japcParameterFactory = ParameterFactory.newInstance();

	  static private final String PROPERTY_START    = "Start";
	  static private final String PROPERTY_STOP     = "Stop";
	  static private final String PROPERTY_ARM      = "Arm";
	  static private final String PROPERTY_DISARM   = "Disarm";
	  static private final String PROPERTY_TRIGGER  = "Trigger";
	  

	  static private Logger logger = Logger.getLogger(ControlProvider.class);

	  public ControlProvider(String device)
	  {
	    this.device = device;
	  }

	  public String getDeviceName() 
	  {
	    return device;
	  }

	  public void powerOn() 
	  {
	    if (device == null || device.length() == 0) 
	    {
	      logger.error("No device specfied!");
	      return;
	    }
	    try
	    {
	      de.gsi.sd.common.japc.ControlProvider.powerOn(device);
	    } 
	    catch (Exception e) 
	    {
	      logger.error("Failed to power on the device "+device,e);
	      DialogManager.showErrorDialog("Failed to power on the device "+device+"!\nReason: "+e.getMessage(),e);
	    }
	  }

	  public void powerOff() 
	  {
	    if (device == null || device.length() == 0) 
	    {
	      logger.error("No device specfied!");
	      return;
	    }
	    try
	    {
	      de.gsi.sd.common.japc.ControlProvider.powerOff(device);
	    }
	    catch (Exception e) 
	    {
	      logger.error("Failed to power off the device "+device,e);
	      DialogManager.showErrorDialog("Failed to power off the device "+device+"!\nReason: "+e.getMessage(),e);
	    }
	  }

	  public void startMeasurement() 
	  {
	    if (device == null || device.length() == 0) 
	    {
	      logger.error("No device specfied!");
	      return;
	    }
	    ParameterUrl url = new ParameterUrlImpl(device,PROPERTY_START);
	    Selector selector = ParameterValueFactory.newSelector(null);
	    try
	    {
	      Parameter p = japcParameterFactory.newParameter(url);
	      MapParameterValue map = ParameterValueFactory.newParameterValue();
	      p.setValue(selector,map);
	      logger.info("Set property "+PROPERTY_START);
	    } 
	    catch (Exception e) 
	    {
	      logger.error("Failed to start measurement on device "+device,e);
	      DialogManager.showErrorDialog("Failed to start measurement on device "+device+"!\nReason: "+e.getMessage(),e);
	    }
	  }

	  public void stopMeasurement() 
	  {
	    if (device == null || device.length() == 0) 
	    {
	      logger.error("No device specfied!");
	      return;
	    }
	    ParameterUrl url = new ParameterUrlImpl(device,PROPERTY_STOP);
	    Selector selector = ParameterValueFactory.newSelector(null);
	    try
	    {
	      Parameter p = japcParameterFactory.newParameter(url);
	      MapParameterValue map = ParameterValueFactory.newParameterValue();
	      p.setValue(selector,map);
	      logger.info("Set property "+PROPERTY_STOP);
	    } 
	    catch (Exception e) 
	    {
	      logger.error("Failed to stop measurement on device "+device,e);
	      DialogManager.showErrorDialog("Failed to stop measurement on device "+device+"!\nReason: "+e.getMessage(),e);
	    }
	  }

	  public void arm() 
	  {
	    if (device == null || device.length() == 0) 
	    {
	      logger.error("No device specfied!");
	      return;
	    }
	    ParameterUrl url = new ParameterUrlImpl(device,PROPERTY_ARM);
	    Selector selector = ParameterValueFactory.newSelector(null);
	    try
	    {
	      Parameter p = japcParameterFactory.newParameter(url);
	      MapParameterValue map = ParameterValueFactory.newParameterValue();
	      p.setValue(selector,map);
	      logger.info("Set property "+PROPERTY_ARM);
	    } 
	    catch (Exception e) 
	    {
	      logger.error("Failed to arm device "+device,e);
	      DialogManager.showErrorDialog("Failed to arm device "+device+"!\nReason: "+e.getMessage(),e);
	    }
	  }

	  public void disarm() 
	  {
	    if (device == null || device.length() == 0) 
	    {
	      logger.error("No device specfied!");
	      return;
	    }
	    ParameterUrl url = new ParameterUrlImpl(device,PROPERTY_DISARM);
	    Selector selector = ParameterValueFactory.newSelector(null);
	    try
	    {
	      Parameter p = japcParameterFactory.newParameter(url);
	      MapParameterValue map = ParameterValueFactory.newParameterValue();
	      p.setValue(selector,map);
	      logger.info("Set property "+PROPERTY_DISARM);
	    } 
	    catch (Exception e) 
	    {
	      logger.error("Failed to disarm device "+device,e);
	      DialogManager.showErrorDialog("Failed to disarm device "+device+"!\nReason: "+e.getMessage(),e);
	    }
	  }

	  public void trigger() 
	  {
	    if (device == null || device.length() == 0) 
	    {
	      logger.error("No device specfied!");
	      return;
	    }
	    ParameterUrl url = new ParameterUrlImpl(device,PROPERTY_TRIGGER);
	    Selector selector = ParameterValueFactory.newSelector(null);
	    try
	    {
	      Parameter p = japcParameterFactory.newParameter(url);
	      MapParameterValue map = ParameterValueFactory.newParameterValue();
	      p.setValue(selector,map);
	      logger.info("Set property "+PROPERTY_TRIGGER);
	    } 
	    catch (Exception e) 
	    {
	      logger.error("Failed to trigger device "+device,e);
	      DialogManager.showErrorDialog("Failed to trigger device "+device+"!\nReason: "+e.getMessage(),e);
	    }
	  }

	  public void reset() 
	  {
	    if (device == null || device.length() == 0) 
	    {
	      logger.error("No device specfied!");
	      return;
	    }
	    try
	    {
	      de.gsi.sd.common.japc.ControlProvider.reset(device);
	    } 
	    catch (Exception e) 
	    {
	      logger.error("Failed to reset device "+device,e);
	      DialogManager.showErrorDialog("Failed to reset device "+device+"!\nReason: "+e.getMessage(),e);
	    }
	  }




}
