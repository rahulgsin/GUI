package de.gsi.sd.BBQ_Proto1.data.filter;

public class DataFilterOptions {

  private String source;
  private String destination;
  
  public DataFilterOptions(String source, String destination)
  {
    this.source = source;
    this.destination = destination;
  }

  /**
   * @return the source
   */
  public String getSource() 
  {
    return source;
  }

  /**
   * @return the destination
   */
  public String getDestination() 
  {
    return destination;
  }
  
  
  
}
