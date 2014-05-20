/*****************************************************************************
 *                                                                           *
 * PrimerExample1 - JAPC Data Provider Listener                              *
 *                                                                           *
 * modified: 2010-08-04 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.data.provider;

import java.util.EventListener;

import de.gsi.sd.BBQ_Proto1.data.FESAData;

/**
 * This interface must be implemented by all classes, which want to listen to
 * new data from the JAPCDataProvider
 */
public interface JAPCDataProviderListener extends EventListener {

  /**
   * Method called by the JAPCDataProvider when new data is available
   * @param data new FESA data
   */
  public void dataReceived(FESAData data);
  
}
