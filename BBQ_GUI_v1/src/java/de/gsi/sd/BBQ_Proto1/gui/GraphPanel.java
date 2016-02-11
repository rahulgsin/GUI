/*****************************************************************************
 *                                                                           *
 * FCT - Graph display panel                                                 *
 *                                                                           *
 * modified: 2012-06-14 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.gui;

import cern.jdve.Axis;
import cern.jdve.Chart;
import cern.jdve.data.DataSet;
import cern.jdve.data.DataSource;
import cern.jdve.data.DefaultDataSet3D;
import cern.jdve.data.DefaultDataSource;
import cern.jdve.data.MountainDataSource;
import cern.jdve.event.AxisEvent;
import cern.jdve.event.AxisListener;
import cern.jdve.event.ChartInteractionEvent;
import cern.jdve.event.ChartInteractionListener;
import cern.jdve.interactor.ZoomInteractor.ZoomInteractionEvent;
import cern.jdve.utils.DataRange;
import cern.jdve.utils.DataWindow;
import cern.jdve.utils.EpsGraphics2D;
import de.gsi.sd.common.controls.PrintPages;
import de.gsi.sd.common.controls.SDPanel;
import de.gsi.sd.common.controls.Updatable;
import de.gsi.sd.BBQ_Proto1.data.AbstractDataDoubleSet;
import de.gsi.sd.BBQ_Proto1.data.BBQData;
//import de.gsi.sd.BBQ_Proto1.data.FCTData.FCTDataSetContainer;
import de.gsi.sd.BBQ_Proto1.data.BBQData.FESADataSetContainer;
import de.gsi.sd.BBQ_Proto1.data.TimeDomainDataSet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
@Updatable
public class GraphPanel extends SDPanel implements ChartInteractionListener, AxisListener {

  static public final int CHART_FCT = 0;
  static public final int CHART_HF  = 1;
  static public final int CHART_USER1 = 2;
  static public final int CHART_USER2 = 3;
  static public final int CHART_MAX = 4;
  static public final int CONTOUR_CHART_MAX = 2;

  private ChartPanel[] chart = new ChartPanel[CHART_MAX];
  private ContourPanel[] contourChart = new ContourPanel[CONTOUR_CHART_MAX];
  private Chart activeChart;
  static private final int FOCUS_BORDER_WIDTH = 2;
  private double [] z;
  private double [] xValues;
  private double [] yValues;
  private double [] zValues;
  private double [] xValues1;
  private double [] yValues1;
  private double [] zValues1;
  static private Logger logger = Logger.getLogger(GraphPanel.class);

  public GraphPanel() 
  {
    super();
    initComponents();
  }

  public void updateData(BBQData data, int [] channel, int [] type)
  {
	  System.out.println("Data refreshed");
 //  BBQ_GUIApplication.getLogger().info("Updating data in Graphpanel");
    for (int i=0;i<chart.length/2;i++)
    {
      setDataSets(data,chart[i],channel[i],type[i]);
    }
    
    for (int i=2;i<chart.length;i++)
    {
      setParDataSets(data,chart[i],i);
    }
    
    for (int i=0;i<CONTOUR_CHART_MAX;i++)
    {
      setDataSets(data,contourChart[i],channel[i],type[i]);
    }
 //   addGraph(data,data.CHANNEL_1_TIME);
  }
  public void updateData(BBQData data, int [] channel, int [] type, int specNumber)
  {
//	  System.out.println("Partial Data refreshed");
 //  BBQ_GUIApplication.getLogger().info("Updating data in Graphpanel");
    for (int i=2;i<chart.length;i++)
    {
      setParDataSets(data,chart[i],i);
    }
 //   addGraph(data,data.CHANNEL_1_TIME);
    
  }

  
  public void layout(int rows, int cols)
  {
    removeAll();
    setLayout(new GridLayout(rows+1,cols));
    contourChart = new ContourPanel[CONTOUR_CHART_MAX];
    chart = new ChartPanel[rows*cols];
    for (int i=0;i<chart.length;i++)
    {
      chart[i] = createChart();
      add(chart[i]);
    }
    for (int i=0;i<CONTOUR_CHART_MAX;i++)
    {
    	contourChart[i] = createContourChart();
    	add(contourChart[i]);
    }
    activeChart = chart[0].getChart();
    chart[0].setBorder(BorderFactory.createLineBorder(Color.BLACK,FOCUS_BORDER_WIDTH));
    fireEvent(BBQGUIEvent.ACTION_DISPLAY_FOCUS,activeChart);
  }

  public void addGraph(BBQData data, String name)
  {
    if (activeChart == null) return;
    DataSource src = activeChart.getDataSource();
    if (src == null) 
    {
      FESADataSetContainer c = data.getDataSet(name);
      if (c.getDataSets().length > 1)
        src = new MountainDataSource(c.getDataSets());
      else
        src = new DefaultDataSource(c.getDataSet());
      activeChart.setDataSource(src);
    }
    else
    {
      FESADataSetContainer c = data.getDataSet(name);
      DataSet[] sets = src.getDataSets();
      if (c.getDataSets().length > 1 && sets.length == 0)
      {
        MountainDataSource msrc = new MountainDataSource(c.getDataSets());
        AbstractDataDoubleSet set = c.getDataSet();
        msrc.setXOffset(set.getX(10)-set.getX(0));
        msrc.setXOffset(0);
        msrc.setYOffset(10.0);
        msrc.setStackInReverseOrder(true);
        activeChart.setDataSource(msrc);
      }
      else
      {
        for (int i=0;i<sets.length;i++) if (sets[i].getName().equalsIgnoreCase(name)) return;
        src.addDataSet(c.getDataSet());
      }
    }
    updateTitles(activeChart);
  }

  public void clearGraph()
  {
    if (activeChart == null) return;
    activeChart.setDataSource(new DefaultDataSource());
  }

  public void synchronizeAxis(boolean b)
  {
    Chart last = null;
    for (ChartPanel p : chart)
    {
      Chart c = p.getChart();
      DataSource src = c.getDataSource();
      if (src != null && src.getDataSetsCount() > 0)
      {
        DataSet set = src.getDataSet(0);
        if (set instanceof TimeDomainDataSet)
        {
          if (last != null)
          {
            c.synchronizeAxis(last, Axis.X_AXIS,b);
          }
          last = c;
        }
      }
    }
  }

  public void setXRange(DataRange range)
  {
    if (activeChart != null) activeChart.getXAxis().setVisibleRange(range);
  }
  
  public void setYRange(DataRange range)
  {
    if (activeChart != null) activeChart.getYAxis().setVisibleRange(range);
  }
  
  public void setXAutoRange(boolean state)
  {
    if (activeChart != null) 
    {
      if (state) activeChart.getXAxis().setVisibleRange(activeChart.getXAxis().getRange());
      activeChart.getXAxis().setAutoVisibleRange(state);
    }
  }
  
  public void setYAutoRange(boolean state)
  {
    if (activeChart != null) 
    {
      if (state) activeChart.getYAxis().setVisibleRange(activeChart.getYAxis().getRange());
      activeChart.getYAxis().setAutoVisibleRange(state);
    }
  }
  
  @Override
  public PrintPages getPrintPages()
  {
    PrintPages pages = new PrintPages();
    String footer = null;
    //    String footer = String.format("%s %s: Virt.Acc. %d",cycleDate,cycleTime,accelerator);
    /*    
    for (int i=0;i<spillChart.length;i++)
    {
      if (spillChart[i].isVisible()) pages.add(new MultiPageChartPrintable(spillChart[i],detectorButtons[i].getText(),null));
    }
     */    
    String[] labels = new String[chart.length];
    Chart[] charts = new Chart[chart.length];
    for (int i=0;i<chart.length;i++) 
    {
      labels[i] = chart[i].getChart().getDataSet().getName();
      charts[i] = chart[i].getChart();
    }
    pages.add(new SinglePageChartPrintable(charts,labels,null,footer));
    return pages;
  }

  @Override
  public boolean export(File f, String format) 
  {
    try 
    {
      if (format.equals(EXPORT_EPS))
        exportEPS(f);
      else
        exportImage(f,format);
      return true;
    }
    catch (IOException e) 
    {
      logger.error("Export failed",e);
    }
    return false;
  }


  @Override
  public void interactionPerformed(ChartInteractionEvent event) 
  {
    if (event instanceof ZoomInteractionEvent)
    {
      ZoomInteractionEvent e = (ZoomInteractionEvent)event;
      DataWindow w = e.getDestDataWindow();
      Chart ch = event.getInteractor().getChart();
      DataSource src = ch.getDataSource();
      DataSet[] sets = src.getDataSets();
      int reduction = 1;
      for (DataSet set : sets)
      {
        if (set instanceof AbstractDataDoubleSet)
        {
          int r = ((AbstractDataDoubleSet)set).calculateReduction(w);
          if (r > reduction) reduction = r;
        }
      }
      for (DataSet set : sets)
      {
        if (set instanceof AbstractDataDoubleSet)
        {
          ((AbstractDataDoubleSet)set).setReduction(reduction);
        }
      }
    }
  }

  @Override
  public void axisChanged(AxisEvent arg0) 
  {
    fireEvent(BBQGUIEvent.ACTION_DISPLAY_RANGE,activeChart);
  }




  private void initComponents() 
  {
    setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    setPreferredSize(new Dimension(1024,800));
    layout(CHART_MAX/2,CHART_MAX/2);
  }

  private ChartPanel createChart()
  {
    ChartPanel panel = new ChartPanel();
    Chart ch = panel.getChart();
    ch.getArea().addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e)
      {
        focusChart(e);
      }
    });
    ch.getXAxis().addAxisListener(this);
    ch.getYAxis().addAxisListener(this);
    return panel;
  }
  
  private ContourPanel createContourChart()
  {
	  ContourPanel panel = new ContourPanel();
	  Chart ch = panel.getChart();
    return panel;
  }

  private void focusChart(MouseEvent e)
  {
    for (int i=0;i<chart.length;i++)
    {
      if (e.getSource() != chart[i].getChart().getArea())
        chart[i].setBorder(BorderFactory.createEmptyBorder(FOCUS_BORDER_WIDTH,FOCUS_BORDER_WIDTH,FOCUS_BORDER_WIDTH,FOCUS_BORDER_WIDTH));
      else
      {
        chart[i].setBorder(BorderFactory.createLineBorder(Color.BLACK,FOCUS_BORDER_WIDTH));
        activeChart = chart[i].getChart();
      }
    }
    fireEvent(BBQGUIEvent.ACTION_DISPLAY_FOCUS,activeChart);
  }

  private void setDataSets(BBQData data, ChartPanel panel, int index, int type)
  {
    Chart ch = panel.getChart();
  //  System.out.println(ch.getRenderersCount());
  //  System.out.println(ch.getYAxisCount());
    DataSource src = ch.getDataSource();
   if (src == null)
    {
     System.out.println("GraphPanel:: Updatedata First time");
    }
     src = new DefaultDataSource();
      switch (index)
      {
      case 1:
    	  if (type ==0)
    	  {
        src.addDataSet(data.getDataSet(BBQData.CHANNEL_1_TIME).getDataSet());
    	 // System.out.println("2nd channel"+ Arrays.toString(data.getDataSet(FESAData.CHANNEL_1_TIME).getDataSet().getData()));
       }
    	  else
        src.addDataSet(data.getDataSet(BBQData.CHANNEL_1_FRE).getDataSet());	  
        break;
      case 2:
    	  if (type ==0)
    	  {
        src.addDataSet(data.getDataSet(BBQData.CHANNEL_2_TIME).getDataSet());
    	 // System.out.println("2nd channel"+ Arrays.toString(data.getDataSet(FESAData.CHANNEL_2_TIME).getDataSet().getData()));
    	  }
          else
          {
        src.addDataSet(data.getDataSet(BBQData.CHANNEL_2_FRE).getDataSet());
    	 // System.out.println("2nd channel frequency"+ Arrays.toString(data.getDataSet(FESAData.CHANNEL_2_FRE).getDataSet().getData()));
          }
    	  break;
      case 3:
   // 	  if (type ==0)
    //    src.addDataSet(data.getDataSet(FESAData.CHANNEL_3_TIME).getDataSet());
    //	  else
        src.addDataSet(data.getDataSet(BBQData.CHANNEL_1_FRE).getDataSet());	  
        break;
      case 4:
    //	  if (type ==0)
    //    src.addDataSet(data.getDataSet(FESAData.CHANNEL_4_TIME).getDataSet());
    //      else
        src.addDataSet(data.getDataSet(BBQData.CHANNEL_1_FRE).getDataSet());	  
        break;
    /*  case 2:
        src.addDataSet(data.getDataSet(FESAData.CHANNEL_2_TIME).getDataSet());
        break;
      case 3:
        src.addDataSet(data.getDataSet(FESAData.CHANNEL_2_FRE).getDataSet());
        break;*/
      }
      ch.setDataSource(src);
 //   }
 /*   else
    {
    	System.out.println("Second time");
      DataSet[] sets = src.getDataSets();
      String[] names = new String[sets.length];
      for (int i=0;i<sets.length;i++)
      {
        names[i] = sets[i].getName();
      }
      FESADataSetContainer[] containers = data.getDataSets(names);
      ArrayList<AbstractDataDoubleSet> list = new ArrayList<AbstractDataDoubleSet>();
      for (FESADataSetContainer c : containers)
      {
        list.addAll(Arrays.asList(c.getDataSets()));
      }
      src.setDataSets(list.toArray(new AbstractDataDoubleSet[list.size()]));*/
    //  BBQ_GUIApplication.getLogger().info("setting the data");
    //  BBQ_GUIApplication.getLogger().info(sets[0].getDataCount());
     /* if (index == 0)
      {
      check_data = ((AbstractDataDoubleSet)sets[0]).getData();
      System.out.println(Arrays.toString(check_data));
      }*/
  //    ch.setDataSource(src);
  //  }
    updateTitles(ch);
    DataSet[] sets = src.getDataSets();
   // System.out.println("I send to Y");
   // System.out.println(((AbstractDataDoubleSet)sets[0]).getY(10));
   // System.out.println(sets[0].getX(20));
   
    //System.out.println(sets[0].getX(20));
/*    if (sets != null && sets.length > 0)
    {
      panel.setReduction(((AbstractDataDoubleSet)sets[0]).getReduction());
    }
    */  // Removed the reduction part from chartpanel-- R. Singh
    
  }

  private void setDataSets(BBQData data, ContourPanel panel, int index, int type)
  {
    Chart ch = panel.getChart();
  //  System.out.println(ch.getRenderersCount());
  //  System.out.println(ch.getYAxisCount());
  /* DefaultDataSet3D src = ch.getDataSource();
   if (src == null)
    {
     System.out.println("GraphPanel:: Updatedata First time");
    }*/

    DefaultDataSet3D src = new DefaultDataSet3D("Quad Signal");
      switch (index)
      {
      case 1:
    	  
   	      z= data.getSpec(1);
	      //zValues = Arrays.copyOfRange(z, 0, 300*512);
   	      zValues = new double[300*512];
	      for (int i = 0;i< 512;i++)
	      {
	    	  for (int j = 0;j< 300;j++)
	    	  {
	    	  zValues[i*300+j] = z[j*512+i];
	    	  }
	      }
	      System.out.println(z[512]);
	      System.out.println(zValues[1]);
	      System.out.println(z[1024]);
	      System.out.println(zValues[2]);
	      System.out.println(zValues.length);

	            xValues = new double[512];
	            double temp = 0;
	            double fftLength = 1024;
	        for (int i = 0; i < xValues.length; i++) {    	
	            xValues[i] = (temp/fftLength);
	            temp++;
	        }
	        
            yValues = new double[300];
        for (int i = 0; i < yValues.length; i++) {
            yValues[i] = i;
        }
   /*    z = data.getSpec(1);
        System.out.println(z.length);
     //   System.out.println("Length of z");
     //   System.out.println(z.length);
        zValues = Arrays.copyOfRange(z, 0, 300*512);
	        System.out.println(zValues.length);

        yValues = new double[300];
        for (int i = 0; i < yValues.length; i++) {
            yValues[i] = i;
        }
        double temp = 0;
        double fftLength = 1024;
        xValues = new double[512];
        for (int i = 0; i <xValues.length; i++) {
            xValues[i] =temp/fftLength ;
            temp++;
        }
        */
        src.set(yValues, xValues, zValues, false, false);
        ch.setDataSet(src);
        break;
      case 2:
      {
    //    src.addDataSet(data.getDataSet(BBQData.CHANNEL_2_FRE).getDataSet());
    	      z= data.getSpec(2);
    	      zValues1 = new double[300*512];
    	      //zValues1 = Arrays.copyOfRange(z, 0, 300*512);
    	      for (int i = 0;i<512;i++)
    	      {
    	    	  for (int j = 0;j<300;j++)
    	    	  {
    	    	  zValues1[i*300+j] = z[j*512+i];
    	    	  }
    	      }

    	            xValues1 = new double[512];
    	            temp = 0;
    	            fftLength = 1024;
    	        for (int i = 0; i < xValues1.length; i++) {
    	        	
    	            xValues1[i] = temp/fftLength;
    	            temp++;
    	        }
    	        
	            yValues1 = new double[300];
	        for (int i = 0; i < yValues1.length; i++) {
	            yValues1[i] = i;
	        }
    	        src.set(yValues1, xValues1, zValues1, false, false);
    	        ch.setDataSet(src);
    	  break;
      }
   //   System.out.println(yValues[500]);
   // updateTitles(ch);
   // DataSet[] sets = src.getDataSets();
      }
      }

  
  
  
  private void setParDataSets(BBQData data, ChartPanel panel,int index)
  {
    Chart ch = panel.getChart();
    DataSource src = ch.getDataSource();
   if (src == null)
    {
     System.out.println("Partial Set");
    }
     src = new DefaultDataSource();
      if (index == 2)
        src.addDataSet(data.getDataSet(BBQData.CHANNEL_1_FRE_PAR).getDataSet());	  
      else
        src.addDataSet(data.getDataSet(BBQData.CHANNEL_2_FRE_PAR).getDataSet());
      ch.setDataSource(src);
    updateTitles(ch);  
  }
  
  
  private void updateTitles(Chart chart)
  {
    DataSource src = chart.getDataSource();
    if (src != null)
    {
      DataSet[] sets = src.getDataSets();
      for (DataSet set : sets)
      {
        if (set instanceof AbstractDataDoubleSet)
        {
          chart.setXScaleTitle(((AbstractDataDoubleSet)set).getxAxisTitle());
          chart.setYScaleTitle(((AbstractDataDoubleSet)set).getyAxisTitle());
        }
      }
    }
  }

  private void exportImage(File f, String format) throws IOException
  {
    int n = 0;
    Rectangle bounds = new Rectangle();
    for (int i=0;i<chart.length;i++)
    {
      if (chart[i].isVisible())
      {
        n++;
        Rectangle r = chart[i].getBounds();
        bounds.add(r);
      }
    }
    BufferedImage bi = (BufferedImage)chart[0].createImage(bounds.width,bounds.height);
    Graphics2D g = bi.createGraphics();
    g.setColor(Color.WHITE);
    g.fillRect(0,0,bounds.width,bounds.height);
    if (n > 0)
    {
      int h = bounds.height / n;
      for (int i=0;i<chart.length;i++)
      {
        if (chart[i].isVisible())
        {
          chart[i].getChart().print(g,chart[i].getChart().getBounds());
          g.translate(0,h);
        }
      }
    }
    try 
    {
      OutputStream out = new FileOutputStream(f);
      ImageIO.write(bi,format,out);
      out.close();
    }
    catch (IOException e) 
    {
      throw e;
    } 
    finally 
    {
      g.dispose();
      bi = null;
    }
  }


  private void exportEPS(File f) throws IOException
  {
    EpsGraphics2D g = new EpsGraphics2D();
    for (int i=0;i<chart.length;i++)
    {
      if (chart[i].isVisible())
      {
        Rectangle r = chart[i].getChart().getBounds();
        chart[i].getChart().print(g,r);
        g.translate(0,r.height);
      }
    }
    try {
      OutputStream out = new FileOutputStream(f);
      BufferedWriter writer= new BufferedWriter(new OutputStreamWriter(out));
      writer.write(g.toString());
      writer.flush();
      writer.close();
    } catch (IOException e) {
      throw e;
    } finally {
      g.dispose();
    }
  }



}
