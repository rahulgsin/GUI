/*****************************************************************************
 *                                                                           *
 * FCT - Data filter interface                                               *
 *                                                                           *
 * modified: 2010-12-02 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.data.filter;

import de.gsi.sd.BBQ_Proto1.data.FESAData;

public interface DataFilter {

  public String getName();
  public boolean execute(FESAData data, DataFilterOptions options);
  
}
