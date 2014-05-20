/*****************************************************************************
 *                                                                           *
 * FCT - Filter description                                                  *
 *                                                                           *
 * modified: 2010-10-19 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.data.filter;

public class FilterDescriptor {

  private DataFilter filter;
  private DataFilterOptions options;
  
  public FilterDescriptor(DataFilter filter, DataFilterOptions options)
  {
    this.filter = filter;
    this.options = options;
  }
  
  public String toString()
  {
    if (options.getSource() == null || options.getDestination() == null) return filter.getName();
    return filter.getName() + " [" + options.getSource() + " >> " + options.getDestination() + "]";
  }

  public DataFilterOptions getOptions()
  {
    return options;
  }
  
  /**
   * @return the filter
   */
  public DataFilter getFilter() 
  {
    return filter;
  }
  
  
}
