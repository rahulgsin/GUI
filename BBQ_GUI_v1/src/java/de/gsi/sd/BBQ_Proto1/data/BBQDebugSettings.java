/*****************************************************************************
 *                                                                           *
 * FCT - Debug settings                                                      *
 *                                                                           *
 * modified: 2012-08-28 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.data;

import cern.japc.ValueType;
import de.gsi.sd.common.japc.GenericProperty;

public class BBQDebugSettings extends GenericProperty implements BBQConstants {
  
  static private final String FIELD_DEBUGOUTPUT = "debugOutput";
  static private final String FIELD_ECHOCONSOLE = "echoConsole";
  static private final String FIELD_SERVERPORT  = "logServerPort";
  static private final String FIELD_SERVERNAME  = "logServerName";

  /**
   * @return the debugOutput
   */
  public boolean isDebugOutput() 
  {
    return getBoolean(FIELD_DEBUGOUTPUT);
  }
  
  /**
   * @param debugOutput the debugOutput to set
   */
  public void setDebugOutput(boolean debugOutput) 
  {
    setField(FIELD_DEBUGOUTPUT,debugOutput,ValueType.BOOLEAN);
  }
  
  /**
   * @return the echoConsole
   */
  public boolean isEchoConsole() 
  {
    return getBoolean(FIELD_ECHOCONSOLE);
  }
  
  /**
   * @param echoConsole the echoConsole to set
   */
  public void setEchoConsole(boolean echoConsole) 
  {
    setField(FIELD_ECHOCONSOLE,echoConsole,ValueType.BOOLEAN);
  }

  /**
   * @return the serverPort
   */
  public short getServerPort() 
  {
    return getShort(FIELD_SERVERPORT);
  }

  /**
   * @param serverPort the serverPort to set
   */
  public void setServerPort(short serverPort) 
  {
    setField(FIELD_SERVERPORT,serverPort,ValueType.SHORT);
  }

  /**
   * @return the serverName
   */
  public String getServerName() 
  {
    return getString(FIELD_SERVERNAME);
  }

  /**
   * @param serverName the serverName to set
   */
  public void setServerName(String serverName) 
  {
    setField(FIELD_SERVERNAME,serverName,ValueType.STRING);
  }

  

}
