package de.gsi.sd.BBQ_Proto1.gui;

import javax.swing.event.EventListenerList;

import cern.jdve.event.ChartInteractionEvent;
import cern.jdve.event.ChartInteractionListener;
import cern.jdve.interactor.ZoomInteractor;

/**
 * This subclass of the ZoomInteractor implements the concept
 * of priority listeners. This listeners are called before the
 * regular listeners registered with the ZoomInteractor are
 * called. This is used to inform other objects like the
 * FCTData about the size of the zoom window before the 
 * chart acts on it. It is used to reduce the number of points
 * displayed in large windows.
 * 
 * @author hbr
 *
 */
public class FCTZoomInteractor extends ZoomInteractor {

  private final EventListenerList listenerList = new EventListenerList();

  public FCTZoomInteractor() {
    super();
  }

  public FCTZoomInteractor(boolean zoomAllowed, boolean zoomAllowed2) {
    super(zoomAllowed, zoomAllowed2);
  }

  public void addPriorityChartInteractionListener(ChartInteractionListener listener) {
      this.listenerList.add(ChartInteractionListener.class, listener);
  }

  public void removePriorityChartInteractionListener(ChartInteractionListener listener) {
      this.listenerList.remove(ChartInteractionListener.class, listener);
  }

  public void clearPriorityChartInteractionListenerList() {
    ChartInteractionListener[] listeners = this.listenerList.getListeners(ChartInteractionListener.class);
    for (ChartInteractionListener l : listeners) listenerList.remove(ChartInteractionListener.class,l);
  }
  
  protected void fireChartInteractionEvent(ChartInteractionEvent e) {
    Object[] listeners = this.listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
        if (listeners[i] == ChartInteractionListener.class) {
            ((ChartInteractionListener) listeners[i + 1]).interactionPerformed(e);
        }
    }
    super.fireChartInteractionEvent(e);
}


}
