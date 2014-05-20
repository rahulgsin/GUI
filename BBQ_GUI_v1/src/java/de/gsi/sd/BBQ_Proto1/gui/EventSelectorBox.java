/*****************************************************************************
 *                                                                           *
 * FCT - Custom combobox for event selection                                 *
 *                                                                           *
 * modified: 2009-12-03 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/


package de.gsi.sd.BBQ_Proto1.gui;

import javax.swing.JComboBox;

import de.gsi.sd.common.oper.EventList;

@SuppressWarnings("serial")
public class EventSelectorBox extends JComboBox {

  public EventSelectorBox()
  {
    super(EventList.getEventList());
    setEditable(false);
  }
  
  public void setSelectedEvent(int event)
  {
    int index = EventList.getEventIndex(event);
    if (index >= 0) setSelectedIndex(index);
    
  }

  public int getSelectedEvent()
  {
    String s = (String)getSelectedItem();
    int i = s.indexOf("-");
    s = s.substring(0,i).trim();
    return Integer.parseInt(s);
  }
  
}
