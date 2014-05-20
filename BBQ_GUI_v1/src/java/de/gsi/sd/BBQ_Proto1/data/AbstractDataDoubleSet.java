package de.gsi.sd.BBQ_Proto1.data;


import cern.jdve.data.DataSet;
import cern.jdve.data.EditionHint;
import cern.jdve.event.DataSetEvent;
import cern.jdve.event.DataSetListener;
import cern.jdve.utils.DataRange;
import cern.jdve.utils.DataWindow;
import cern.jdve.utils.IndexWindow;
import cern.jdve.utils.PropertyUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.EventListenerList;

abstract public class AbstractDataDoubleSet implements DataSet {

  /** Sampling mode for display data reduction: take the value in the center of the interval */
  static public final int SAMPLING_MODE_SAMPLE  = 0;
  /** Sampling mode for display data reduction: sum values over the interval */
  static public final int SAMPLING_MODE_BINNING = 1;


  /** Determines if this data set should be visible (drawn) by renderer */
  public static final String PROPERTY_VISIBLE           = "visible";
  /** */
  public static final String PROPERTY_VISIBLE_IN_LEGEND = "visibleInLegend";
  public static final String PROPERTY_STYLE             = "style";

  protected String name;
  protected double[] data;
  protected double sum = 0;
  protected double sum2 = 0;
  protected double frequency;
  protected int reduction = 1;
  protected DataRange yRange;
  protected int samplingMode = SAMPLING_MODE_SAMPLE;
  protected String xAxisTitle = "Time [ms]";
  protected String yAxisTitle = "Data";
  protected final Map<String, Object> properties = new HashMap<String, Object>();
  protected final EventListenerList listenerList = new EventListenerList();

  static protected final int DISPLAY_POINT_COUNT = 10000;

  public AbstractDataDoubleSet(String name) 
  {
    this.name = name;
  }
  
  public int calculateReduction(DataWindow w)
  {
    IndexWindow iw = new IndexWindow();
    iw = getIndexWindow(w,true,iw);
    int n = (iw.getXRange().getMax() - iw.getXRange().getMin()) * reduction;
    int reduction = 1;
    if (n <= DISPLAY_POINT_COUNT)
      reduction = 1;
    else
      reduction = (n + DISPLAY_POINT_COUNT - 1) / DISPLAY_POINT_COUNT;
    if (reduction <= 0) reduction = 1;
    return reduction;
  }
  
  public int calculateReduction(DataRange xRange)
  {
    DataWindow w = new DataWindow(xRange,xRange);
    IndexWindow iw = new IndexWindow();
    iw = getIndexWindow(w,true,iw);
    int n = (iw.getXRange().getMax() - iw.getXRange().getMin()) * reduction;
    int reduction = 1;
    if (n <= DISPLAY_POINT_COUNT)
      reduction = 1;
    else
      reduction = (n + DISPLAY_POINT_COUNT - 1) / DISPLAY_POINT_COUNT;
    if (reduction <= 0) reduction = 1;
    return reduction;
  }
  
  /**
   * @return the frequency
   */
  public double getFrequency() 
  {
    return frequency;
  }

  /**
   * @param frequency the frequency to set
   */
  public void setFrequency(double frequency) 
  {
    this.frequency = frequency;
  }

  /**
   * @return the reduction
   */
  public int getReduction() 
  {
    return reduction;
  }

  /**
   * @param reduction the reduction to set
   */
  public void setReduction(int reduction) 
  {
    this.reduction = reduction;
  }

  /**
   * @return the xAxisTitle
   */
  public String getxAxisTitle() 
  {
    return xAxisTitle;
  }

  /**
   * @param xAxisTitle the xAxisTitle to set
   */
  public void setxAxisTitle(String xAxisTitle) 
  {
    this.xAxisTitle = xAxisTitle;
  }

  /**
   * @return the yAxisTitle
   */
  public String getyAxisTitle() 
  {
    return yAxisTitle;
  }

  /**
   * @param yAxisTitle the yAxisTitle to set
   */
  public void setyAxisTitle(String yAxisTitle) 
  {
    this.yAxisTitle = yAxisTitle;
  }

  /**
   * @return the samplingMode
   */
  public int getSamplingMode() 
  {
    return samplingMode;
  }

  /**
   * @param samplingMode the samplingMode to set
   */
  public void setSamplingMode(int samplingMode) 
  {
    this.samplingMode = samplingMode;
  }

  public double[] getData()
  {
    return data;
  }
  
  /**
   * Allocate memory for the ADC data array with size data points.
   * @param size number of samples for which to allocate memory
   */
  public void allocateData(int size) 
  {
    if (data != null && data.length == size)
    {
      Arrays.fill(data,0.0f);
    }
    else
    {
      data = new double[size];
    }
    sum = 0;
    sum2 = 0;
    yRange = new DataRange();
    /* Assume full scale is displayed */
    reduction = (size + DISPLAY_POINT_COUNT - 1) / DISPLAY_POINT_COUNT;
    if (reduction <= 0) reduction = 1;
    fireDataSetChanged(new DataSetEvent(this,DataSetEvent.FULL_CHANGE));
  }

  /**
   * Set the complete ADC data array
   * @param data the new data array
   */
  public void setData(double[] data) 
  {
    if (this.data == null || this.data.length != data.length)
    {
      /* Assume full scale is displayed */
      reduction = (data.length + DISPLAY_POINT_COUNT - 1) / DISPLAY_POINT_COUNT;
      if (reduction <= 0) reduction = 1;
    }
    this.data = data;
    sum = 0;
    sum2 = 0;
    yRange = new DataRange();
    for (int j=0;j<data.length;j++)
    {
      yRange.add(data[j]);
      sum += data[j];
      sum2 += data[j] * data[j];
    }
/*    
    if (data.length > 1)
    {
      double mean = sum / data.length;
      double sigma = Math.sqrt((data.length*sum2-sum*sum)/data.length/(data.length-1));
    }
*/    
    fireDataSetChanged(new DataSetEvent(this,DataSetEvent.FULL_CHANGE));
  }
  
  /**
   * Allocate memory for the ADC data array with size data points per channel
   * and copy the given array into it. The size of the data array passed
   * can be smaller or larger then the size argument.
   * @param data the new data array to copy
   * @param size number of samples for which to allocate memory
   */
  public void setData(double[] data, int size) 
  {
    if (this.data != null && this.data.length == size)
    {
      Arrays.fill(this.data,0.0f);
      sum = 0;
      sum2 = 0;
      yRange = new DataRange();
      for (int j=0;j<data.length;j++)
      {
        this.data[j] = data[j];
        yRange.add(data[j]);
      }
    }
    else
    {
      this.data = Arrays.copyOf(data,size);
      sum = 0;
      sum2 = 0;
      yRange = new DataRange();
      for (int j=0;j<data.length;j++)
      {
        yRange.add(data[j]);
        sum += data[j];
        sum2 += data[j] * data[j];
      }
      /* Assume full scale is displayed */
      reduction = (size + DISPLAY_POINT_COUNT - 1) / DISPLAY_POINT_COUNT;
      if (reduction <= 0) reduction = 1;
    }
    fireDataSetChanged(new DataSetEvent(this,DataSetEvent.FULL_CHANGE));
  }


  /**
   * Copy the given data array into the current data array starting 
   * at the given next sample
   * @param nextSample sample index where to copy the new data array
   * @param data new data array
   */
  public void appendData(int nextSample, double[] data) 
  {
    for (int j=0;j<data.length;j++)
    {
      this.data[nextSample+j] = data[j];
      yRange.add(data[j]);
      sum += data[j];
      sum2 += data[j] * data[j];
    }
    fireDataSetChanged(new DataSetEvent(this,DataSetEvent.DATA_ADDED,nextSample,nextSample+data.length));
  }
  
  @Override
  public void add(double arg0, double arg1) {
  }

  @Override
  public void add(int arg0, double arg1, double arg2) {
  }

  @Override
  public void addDataSetListener(DataSetListener listener)
  {
    listenerList.add(DataSetListener.class, listener);
  }

  @Override
  public int getDataCount() {
    if (data == null) return 0;
    return (data.length + reduction - 1) / reduction;
  }

  @Override
  public String getDataLabel(int arg0) {
    return null;
  }

  @Override
  public EditionHint getEditionHint() {
    return null;
  }

  @Override
  public IndexWindow getIndexWindow(DataWindow window, boolean includeMarginal, IndexWindow indexWindow) {
    if (!window.isDefined()) {
      throw new IllegalArgumentException("DataWindow must be defined!");
    }
    DataRange xRange = window.getXRange();
    if (indexWindow == null) indexWindow = new IndexWindow();
    int size = getDataCount();
    double minx = xRange.getMin();
    double maxx = xRange.getMax();
    if (size == 0 || minx > getX(size - 1) || maxx < getX(0)) 
    {
      indexWindow.empty();
      return indexWindow;
    }
    int minIndex = findMinXIndex(minx, includeMarginal);
    int maxIndex = findMaxXIndex(maxx, includeMarginal);
    indexWindow.getXRange().set(minIndex, maxIndex);
    indexWindow.getYRange().set(minIndex, maxIndex);
    // The second argument should be computed yRange - but we ignore it
    // If the Y data is not in the visible range - it will be simply painted
    // outside of visible rectangle - it is simpler and faster than finding correct Y range
    return indexWindow;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Object getProperty(String propertyName) {
    synchronized (this) {
      return this.properties.get(propertyName);
    }
  }

  @Override
  public Double getUndefValue() {
    return null; /* render all points */
  }

  @Override
  public double getX(int index) 
  {
//    int x = index * reduction + reduction / 2; /* center of reduction interval */
    int x = index * reduction;
  //  System.out.println("It comes in X so many times");
  //  System.out.println(index);
    return getXValue(x);
  }

  
  @Override
  public DataRange getXRange() {
    return new DataRange(getX(0),getX(getDataCount()-1));
  }

  @Override
  public double getY(int index) {
    if (index < 0) return 0;
   // System.out.println("It comes in Y");
   // System.out.println(index);
    int fromIndex = index * reduction;
    if (fromIndex >= data.length) return 0;
    int toIndex = fromIndex + reduction;
    if (toIndex >= data.length) toIndex = data.length;
    double val = 0.0;
    switch (samplingMode)
    {
    case SAMPLING_MODE_SAMPLE:
      /* just one value from the interval center */      
      val = data[(fromIndex+toIndex)/2];
      break;
    case SAMPLING_MODE_BINNING:
      /* just the sum */
      for (int i=fromIndex;i<toIndex;i++) val += data[i];
      break;
    }
    return val;
  }

  @Override
  public DataRange getYRange() {
    if (yRange == null) return new DataRange(-1.0,1.0);
    return yRange;
  }

  @Override
  public int indexOf(double x) 
  {
    int index = getIndex(x);
    if (Math.abs(index-x) < 1.0E-15) return index / reduction;
    return -index / reduction - 1;
  }

  abstract public int getIndex(double x);
  abstract public double getXValue(int index); 
  abstract public AbstractDataDoubleSet clone(String name);
  

  @Override
  public double interpolate(double x) {
    if (getDataCount() == 0) return Double.NaN;
    int i = indexOf(x);
    // the value returned is index of the existing x-coordinate or
    // -(insertion point) - 1 if the x-coordinate does not exist
    if (i >= 0) {
      // the point does exist
      return getY(i);
    }
    int insertionIndex = -1 - i;
    if (insertionIndex == 0) return Double.NaN;
    if (insertionIndex == getDataCount()) return Double.NaN;
    int index1 = insertionIndex - 1;
    int index2 = insertionIndex;
    double x1 = getX(index1);
    double y1 = getY(index1);
    double x2 = getX(index2);
    double y2 = getY(index2);
    if (Double.isNaN(y1) || Double.isNaN(y2)) {
      // case where the function has a gap (y-coordinate equals to NaN
      return Double.NaN;
    }
    return y2 + (y2 - y1) * (x - x2) / (x2 - x1);
  }

  @Override
  public boolean isEditable() {
    return false;
  }

  @Override
  public boolean isVisible() {
    return PropertyUtils.getBoolean(getProperty(PROPERTY_VISIBLE), true);
  }

  @Override
  public boolean isVisibleLegend() {
    return PropertyUtils.getBoolean(getProperty(PROPERTY_VISIBLE_IN_LEGEND), true);
  }

  @Override
  public void remove(int fromIndex, int toIndex) {
  }

  @Override
  public void removeDataSetListener(DataSetListener listener) {
  }

  @Override
  public void set(int arg0, double arg1, double arg2) {
  }

  public void setProperty(String propertyName, Object value, boolean notify) {
    Object oldValue = null;
    synchronized (this) {
      oldValue = getProperty(propertyName);
      if (oldValue == value) {
        return;
      }
      this.properties.put(propertyName, value);
    }
    if (notify) 
    {
      fireDataSetChanged(new DataSetEvent(this, propertyName, oldValue, value));
    }
  }

  /**
   * Returns index of the biggest X in this DataSet that is smaller then passed minx.
   * 
   * @param minx
   * @param includeMarginal if true the method should return index - 1 if index > 0
   * @return index of biggest X
   */
  protected int findMinXIndex(double minx, boolean includeMarginal) {
    int left = 0, right = getDataCount() - 1;

    if (minx <= getX(left)) {
      return left;
    }

    for (; left <= right;) {
      int index = (left + right) / 2;
      double val = getX(index);

      if (val < minx) {
        left = index + 1;
      } else if (val > minx) {
        right = index - 1;
      } else {
        return index > 0 && includeMarginal ? index - 1 : index;
      }
    }

    return includeMarginal ? right : right + 1;
  }

  /**
   * Returns index of the biggest X in this DataSet that is smaller then passed minx.
   * 
   * @param miny
   * @param includeMarginal if true the method should return index - 1 if index > 0
   * @return index
   */
  protected int findMinYIndex(double miny, boolean includeMarginal) {
    int left = 0, right = getDataCount() - 1;

    if (miny <= getY(left)) {
      return left;
    }

    for (; left <= right;) {
      int index = (left + right) / 2;
      double val = getY(index);

      if (val < miny) {
        left = index + 1;
      } else if (val > miny) {
        right = index - 1;
      } else {
        return index > 0 && includeMarginal ? index - 1 : index;
      }
    }

    return includeMarginal ? right : right + 1;
  }

  /**
   * Returns index of the smallest X that is bigger the the passed maxx.
   * 
   * @param maxx
   * @param includeMarginal if true the method should return index + 1 if index < dataSetSize
   * @return index
   */
  protected int findMaxXIndex(double maxx, boolean includeMarginal) {
    int left = 0, right = getDataCount() - 1;

    if (maxx >= getX(right)) {
      return right;
    }

    for (; left <= right;) {
      int index = (left + right) / 2;
      double val = getX(index);

      if (val < maxx) {
        left = index + 1;
      } else if (val > maxx) {
        right = index - 1;
      } else {
        return index;
      }
    }
    return includeMarginal ? left : left - 1;
  }

  /**
   * Returns index of the smallest X that is bigger the the passed maxx.
   * 
   * @param maxy
   * @param includeMarginal if true the method should return index + 1 if index < dataSetSize
   * @return index
   */
  protected int findMaxYIndex(double maxy, boolean includeMarginal) {
    int left = 0, right = getDataCount() - 1;

    if (maxy >= getY(right)) {
      return right;
    }

    for (; left <= right;) {
      int index = (left + right) / 2;
      double val = getY(index);

      if (val < maxy) {
        left = index + 1;
      } else if (val > maxy) {
        right = index - 1;
      } else {
        return index;
      }
    }
    return includeMarginal ? left : left - 1;
  }

  /**
   * Dispatches given DataSetEvent to all registered listeners if the flag <code>adjusting</code> is set to
   * <code>false</code>.
   * 
   * @param evt
   */
  protected synchronized void fireDataSetChanged(DataSetEvent evt) 
  {
    Object[] listeners = listenerList.getListenerList();
    // Loop over listeners array (using internal format of EventListenerList)
    // Look at the JavaDoc of this class for details
    for (int i = listeners.length - 2; i >= 0; i -= 2) 
    {
      if (listeners[i] == DataSetListener.class) 
      {
        ((DataSetListener) listeners[i + 1]).dataSetChanged(evt);
      }
    }
  }




}

