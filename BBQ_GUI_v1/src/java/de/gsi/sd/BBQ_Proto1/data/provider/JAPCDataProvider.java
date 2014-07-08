/*****************************************************************************
 *                                                                           *
 * PrimerExample1 - JAPC Data Provider                                       *
 *                                                                           *
 * modified: 2010-08-04 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.data.provider;

import javax.swing.event.EventListenerList;

import cern.japc.AcquiredParameterValue;
import cern.japc.ImmutableMapParameterValue;
import cern.japc.Parameter;
import cern.japc.ParameterException;
import cern.japc.ParameterValue;
import cern.japc.ParameterValueListener;
import cern.japc.Selector;
import cern.japc.SimpleParameterValue;
import cern.japc.SubscriptionHandle;
import cern.japc.Type;
import cern.japc.Array2D;
import cern.japc.factory.ParameterFactory;
import cern.japc.factory.ParameterValueFactory;
import cern.japc.spi.ParameterUrl;
import cern.japc.spi.ParameterUrlImpl;
import de.gsi.sd.BBQ_Proto1.BBQ_GUIApplication;
import de.gsi.sd.BBQ_Proto1.data.FESAData;
import de.gsi.sd.BBQ_Proto1.gui.MainPanel;
//import de.gsi.sd.BBQ_Proto1.data.FCTData;

import java.util.Arrays;

/**
 * This class connects to the FESA device and obtains the acquired data 
 * from the Acquisition property. The data can be acquired synchronously
 * or asynchronously via a subscription. In case of a subscription, registered
 * listeners (JAPCDataProviderListener) will be notified when new data is
 * received.
 */
public class JAPCDataProvider  {
	 static public final int OUTPUT_ASCII = 0;
	  
	  static public final int TIMEOUT = 100;
	  static public final int MAX_SAMPLELENGTH = 15000000;
	  
	  static public final int NO_REDUCTION          = 1;
	  static public final int NO_REDUCTION_PARTIAL  = -1;
	  

	 // protected FCTData data = new FCTData();
	  protected int reduction = NO_REDUCTION;

	  static protected final String PROPERTY_DATA = "Acquisition";
	  static protected final String PROPERTY_EXPERTDATA = "ExpertAcquisition";

	  static protected final String FIELD_TIMESTAMP = "timeStamp";
	  static protected final String FIELD_CYCLECOUNTER = "cycleCounter"; 
	  static protected final String FIELD_SAMPLES = "samples";
	  static protected final String FIELD_SAMPLELENGTH = "sampleLength";
	  static protected final String FIELD_SAMPLINGFREQUENCY = "samplingFrequency";
	  static protected final String FIELD_FIRSTSAMPLE = "firstSample";
	  static protected final String FIELD_DATAREDUCTION = "dataReduction";
	  static protected final String FIELD_FIRSTSAMPLEREQUEST = "firstSampleRequest";
	  static protected final String FIELD_SAMPLELENGTHREQUEST = "sampleLengthRequest";
	  static protected final String FIELD_ADCOFFSET = "adcOffset";
	  static protected final String FIELD_ADCRANGE = "adcRange";
	  static protected final String FIELD_ADCVALUEMODE = "adcValueMode";
	  static protected final String FIELD_EVENTS = "event";
	  static protected final String FIELD_DELAYS = "delay";
	  static protected final String FIELD_PREDELAY = "ringbufferPreDelay";
	  static protected final String FIELD_GAIN = "hwGain";
	  static protected final String FIELD_BASELINE = "adcBaseLine";
  /** The name of the FESA device the class is subscribed to */
  private String deviceName = "";
  /** The list of all registered listeners */
  private EventListenerList listenerList = new EventListenerList();
  /** The handle of the data subscription */
  private SubscriptionHandle dataSubscriptionHandle;

  /** The instance of the JAPC parameter factors */
  static private ParameterFactory japcParameterFactory = ParameterFactory.newInstance();

  /** Name of the property */
  static private final String PROPERTY_ACQUISITION = "DataAcquisition";
  /** Name of the data field in the property */
  static private final String FIELD_TIME = "timeData";
  static private final String FIELD_FRE = "freqData";
  FESAData data = new FESAData();
  /**
   * Get the FESA device name, to which this provider is subscribed. 
   * @return the FESA device name
   */
  public String getDeviceName() 
  {
    return deviceName;
  }

  /**
   * Add a data provider listener to the list of registered listeners
   * @param l the data provider listener
   */
  public void addDataProviderListener(JAPCDataProviderListener l) 
  {
    listenerList.add(JAPCDataProviderListener.class,l);
    BBQ_GUIApplication.getLogger().info("Added to listener list");
  } 

  /**
   * Remove a data provider listener from the list of registered listeners
   * @param l the data provider listener
   */
  public void removeDataProviderListener(JAPCDataProviderListener l) 
  {
    listenerList.remove(JAPCDataProviderListener.class,l);
    BBQ_GUIApplication.getLogger().info("Removed from listener list");
  }

  /**
   * Check if the data provider is subscribed to a FESA device
   * @return true is the data provider is subscribed
   */
  public boolean isSubscribedData()
  {
    if (dataSubscriptionHandle == null) return false;
    return dataSubscriptionHandle.isMonitoring();
  }

  /**
   * Subscribe to the data of a specified FESA device. The data provider will
   * then be automatically notified if the data of the FESA device changes.
   * The provider will retrieve the new data and notify all registered 
   * listeners.
   * @param deviceName the FESA device name
   * @return true on success
   */
  public boolean subscribe(String deviceName)
  {
    this.deviceName = deviceName;
    /* Check for device name */
    if (deviceName == null || deviceName.isEmpty())
    {
    	BBQ_GUIApplication.getLogger().error("No device specfied!");
      return false;
    }
    /* Get a URL for a parameter reading the acquisition property */
    ParameterUrl url = new ParameterUrlImpl(deviceName,PROPERTY_ACQUISITION);
    /* Get a cycle selector for not multiplexed device */
    Selector selector = ParameterValueFactory.newSelector(null);
    try 
    {
      /* Get a parameter for the acquisition property */
      Parameter p = japcParameterFactory.newParameter(url);
      /* Create a subscription to the property with a new subscription handler */
      dataSubscriptionHandle = p.createSubscription(selector,new SubscriptionHandler());
      /* Start monitoring on the subscription */
      dataSubscriptionHandle.startMonitoring();
      BBQ_GUIApplication.getLogger().info("Created and started subscription on parameter "+p.getName());
    } 
    catch (Exception e) 
    {
    	BBQ_GUIApplication.getLogger().error("Failed to create subscription", e);
      return false;
    }
    return true;
  }

  /**
   * Unsubscribe from the data of the FESA device
   * @return true on success 
   */
  public boolean unsubscribe()
  {
    
    /* Log a warning if there is no subscription */
    if (dataSubscriptionHandle == null)
    {
    	BBQ_GUIApplication.getLogger().warn("No subscription to unsubscribe!");
      return true;
    }
    /* stop monitoring on the subscription */
    dataSubscriptionHandle.stopMonitoring();
    /* invalidate the subscription handle */
    BBQ_GUIApplication.getLogger().info("Stopped subscription on parameter "+dataSubscriptionHandle.getParameter().getName());
    dataSubscriptionHandle = null;
    deviceName = null; 
    return true;
  }  

  /**
   * Request synchronous retrieval of data from the FESA device and notify 
   * all registered listeners of the new data. 
   * @param deviceName the FESA device name
   */
  public void getData(String deviceName) 
  {
    /* Check for device name */
    if (deviceName == null || deviceName.isEmpty()) 
    {
    	BBQ_GUIApplication.getLogger().error("No device specified!");
      return;
    }
    /* Get a URL for a parameter reading the acquisition property */
    ParameterUrl url = new ParameterUrlImpl(deviceName, PROPERTY_ACQUISITION);
    /* Get a cycle selector for not multiplexed device */
    Selector selector = ParameterValueFactory.newSelector(null);
    try 
    {
      /* Get a parameter for the acquisition property */
      Parameter p = japcParameterFactory.newParameter(url);
      /* Get the data from the FESA class */
      AcquiredParameterValue acquiredValue = p.getValue(selector);
      /* Parse the acquired data into the FESAData class */
      FESAData data = getData(acquiredValue);
      /* Notify listeners if new data is available */
      if (data != null) fireEvent(data);
    }
    catch (Exception e) 
    {
    	BBQ_GUIApplication.getLogger().error("Failed to get data from device " + deviceName, e);
    }
  }

  /**
   * Get the FESA data from the acquired parameter value. This method is 
   * usually called by the subscription handler after receiving a 
   * notification from the FESA device.
   * @param acquiredValue acquired parameter value
   * @return the FESA data object
   */
  synchronized private FESAData getData(AcquiredParameterValue acquiredValue)
  {
    /* Get the actual parameter value, i.e. data */
    ParameterValue value = acquiredValue.getValue();
    /* If the property has more than one field the parameter value will be of
     * type MAP */
    if (value.getType().equals(Type.MAP))
    {
    	
      /* Cast the parameter value to its correct class of type MAP */
      ImmutableMapParameterValue map = (ImmutableMapParameterValue)value;
      /* Obtain the value of the field given by FIELD_VALUE (i.e. "value") as
       * a double value */
    //  double[] data_time = map.getDoubles(FIELD_TIME);
      Array2D data_time = map.getArray2D(FIELD_TIME);
      Array2D data_fre = map.getArray2D(FIELD_FRE);
      /* Create the FESA data object */
      BBQ_GUIApplication.getLogger().info("Data is mapped");
   //   System.out.println(Arrays.toString(data_time.getDoubleArray2D()));
     // System.out.println(Arrays.toString(data_time.getDoubleRow(10)));
      // System.out.println(Arrays.toString(data_fre.getDoubles()));
      data.FESAStoreData(data_time,data_fre);
      return data;
    }
    /* If the property has only one field the parameter value will be of
     * type SIMPLE */
 //   else if (value.getType().equals(Type.SIMPLE))
  //  {
      /* Obtain the single value as a double value */ 
   //   double[] v = ((SimpleParameterValue)value).getDoubles();
      /* Create the FESA data object */
  /*    BBQ_GUIApplication.getLogger().info("Data is simple");
      System.out.println(v);
      FESAData data = new FESAData(v);
      return data;
    }*/
    /* unhandled parameter value types: no FESA data */
    return null;
  }


  /**
   * Notify all registered listeners about new data
   * @param data the new FESA data
   */
  synchronized private void fireEvent(FESAData data)
  {
    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length-2; i>=0; i-=2) 
    {
      if (listeners[i] == JAPCDataProviderListener.class) 
      {
        ((JAPCDataProviderListener)listeners[i+1]).dataReceived(data);
      }
    }
  }


  /**
   * The SubscriptionHandler class is a utility class which provides the
   * required call back functions for the JAPC subscription mechanism.
   */
  private class SubscriptionHandler implements ParameterValueListener {

    /**
     * Method called when an exception occurred
     * @param parameterName name of the parameter (property) whose exception is received
     * @param description the description of the exception
     * @param exception the complete exception object
     */
    public void exceptionOccured(String parameterName, String description, ParameterException exception)
    {
    	BBQ_GUIApplication.getLogger().error(parameterName+": "+description,exception);
    }

    /**
     * Method called when new data is received.
     * @param parameterName name of the parameter (property) whose data is received
     * @param value the acquired parameter data
     */
    public void valueReceived(String parameterName, AcquiredParameterValue value)
    {
     BBQ_GUIApplication.getLogger().info("Data is coming");
      FESAData data = getData(value);
      if (data != null) fireEvent(data);
    }
  }



}
