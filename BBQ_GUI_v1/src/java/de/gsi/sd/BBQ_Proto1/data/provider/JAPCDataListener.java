/*****************************************************************************
 *                                                                           *
 * PrimerExample1 - JAPC Data Provider Listener                              *
 *                                                                           *
 * modified: 2010-08-04 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.data.provider;

import de.gsi.sd.BBQ_Proto1.data.FESAData;

/**
 * This interface must be implemented by all classes, which want to listen to
 * new data from the JAPCDataProvider
 */
public class JAPCDataListener implements JAPCDataProviderListener {

  /**
   * Method called by the JAPCDataProvider when new data is available
   * @param data new FESA data
   */
  public void dataReceived(FESAData data)
  {
	  System.out.println("Why should I come here?");
  }
}
