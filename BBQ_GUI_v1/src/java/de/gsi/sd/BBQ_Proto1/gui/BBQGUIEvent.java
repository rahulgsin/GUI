/*****************************************************************************
 *                                                                           *
 * FCT - GUI event                                                           *
 *                                                                           *
 * modified: 2011-07-19 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.gui;

import de.gsi.sd.common.controls.GUIEvent;

@SuppressWarnings("serial")
public class BBQGUIEvent extends GUIEvent {

  static public final int ACTION_SUBSCRIBE            = USER + 1; 
  static public final int ACTION_UNSUBSCRIBE          = USER + 2; 
  static public final int ACTION_SETTINGS_SET         = USER + 3;
  static public final int ACTION_SETTINGS_GET         = USER + 4;
  static public final int ACTION_EXPERTSETTINGS_SET   = USER + 5;
  static public final int ACTION_EXPERTSETTINGS_GET   = USER + 6;
  static public final int ACTION_GURUSETTINGS_SET     = USER + 7;
  static public final int ACTION_GURUSETTINGS_GET     = USER + 8;
  static public final int ACTION_DEBUGSETTINGS_SET    = USER + 9;
  static public final int ACTION_DEBUGSETTINGS_GET    = USER + 10;
  static public final int ACTION_MEASUREMENT          = USER + 11;
  
  static public final int ACTION_START                = USER + 50;
  static public final int ACTION_STOP                 = USER + 51;
  static public final int ACTION_POWERON              = USER + 52;
  static public final int ACTION_POWEROFF             = USER + 53;
  static public final int ACTION_RESET                = USER + 54;
  static public final int ACTION_ARM                  = USER + 55;
  static public final int ACTION_DISARM               = USER + 56;
  static public final int ACTION_TRIGGER              = USER + 57;
  
  static public final int ACTION_ANALYSIS_STATISTICS  = USER + 100;

  static public final int ACTION_DISPLAY_GRAPH        = USER + 200;
  static public final int ACTION_DISPLAY_LAYOUT       = USER + 201;
  static public final int ACTION_DISPLAY_CLEAR        = USER + 202;
  static public final int ACTION_DISPLAY_SYNC         = USER + 203;
  static public final int ACTION_DISPLAY_FOCUS        = USER + 204;
  static public final int ACTION_DISPLAY_RANGE        = USER + 205;
  static public final int ACTION_DISPLAY_SETXRANGE    = USER + 206;
  static public final int ACTION_DISPLAY_SETYRANGE    = USER + 207;
  static public final int ACTION_DISPLAY_AUTOXRANGE   = USER + 208;
  static public final int ACTION_DISPLAY_AUTOYRANGE   = USER + 209;
  
  
  static public final int ACTION_FILTER_ADD           = USER + 250;
  static public final int ACTION_FILTER_REMOVE        = USER + 251;
  static public final int ACTION_FILTER_EDIT          = USER + 252;

  public BBQGUIEvent(Object source, int cmd) 
  {
    super(source,cmd);
  }

  public BBQGUIEvent(Object source, int cmd, Object data) 
  {
    super(source,cmd,data);
  }

  
}
