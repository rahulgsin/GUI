package de.gsi.sd.BBQ_Proto1.gui;

import cern.jdve.Chart;
import cern.jdve.ChartRenderer;
import cern.jdve.data.DataSet;
import cern.jdve.data.DataSource;
import cern.jdve.event.AxisEvent;
import cern.jdve.event.AxisListener;
import cern.jdve.event.ChartInteractionEvent;
import cern.jdve.event.ChartInteractionListener;
import cern.jdve.interactor.DataPickerInteractor;
import cern.jdve.interactor.ZoomInteractor.ZoomInteractionEvent;
import cern.jdve.renderer.PolylineChartRenderer;
import cern.jdve.utils.DataWindow;
import de.gsi.sd.common.controls.SDPanel;
import de.gsi.sd.common.controls.localizable.LocalizableLabel;
import de.gsi.sd.common.language.Language;
import de.gsi.sd.BBQ_Proto1.data.AbstractDataSet;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ChartPanel extends SDPanel implements ChartInteractionListener, AxisListener {

  private Chart chart;
  
  static private final int FOCUS_BORDER_WIDTH = 2;
  
  public ChartPanel() 
  {
    super();
    initComponents();
  }

  public Chart getChart()
  {
    return chart;
  }
  
  
  @Override
  public void interactionPerformed(ChartInteractionEvent event) 
  {
    if (event instanceof ZoomInteractionEvent)
    {
      ZoomInteractionEvent e = (ZoomInteractionEvent)event;
      DataWindow w = e.getDestDataWindow();
      Chart chart = event.getInteractor().getChart();
      DataSource src = chart.getDataSource();
      DataSet[] sets = src.getDataSets();
      int reduction = 1;
      for (DataSet set : sets)
      {
        if (set instanceof AbstractDataSet)
        {
          int r = ((AbstractDataSet)set).calculateReduction(w);
          if (r > reduction) reduction = r;
        }
      }
    }
  }

  
  private void initComponents()
  {
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(FOCUS_BORDER_WIDTH,FOCUS_BORDER_WIDTH,FOCUS_BORDER_WIDTH,FOCUS_BORDER_WIDTH));
    {
      chart = new Chart();
      add(chart,BorderLayout.CENTER);
      chart.setDoubleBuffered(false);
      ChartRenderer chartRenderer = new PolylineChartRenderer();
      chart.addRenderer(chartRenderer);
      chart.setXScaleTitle(Language.getInstance().getString("Chart.XSamples"));
      chart.setYScaleTitle(Language.getInstance().getString("Chart.Y"));
      DataPickerInteractor dataPicker = new DataPickerInteractor();
      //          dataPicker.setCrossPainted(true);
      chart.addInteractor(dataPicker);
      FCTZoomInteractor zoomInteractor = new FCTZoomInteractor(true,true);
      zoomInteractor.setAnimationStep(0);
//      zoomInteractor.addPriorityChartInteractionListener(this);
      chart.addInteractor(zoomInteractor);
      chart.getArea().setLeftMargin(100);
      chart.getArea().setRightMargin(20);
      chart.setXRange(0,1);
      chart.setYRange(0,4095);
      chart.getXAxis().setAutoRange(true);
      chart.getXAxis().addAxisListener(this);
      chart.getYAxis().setAutoRange(true);
    }
 
  }

  @Override
  public void axisChanged(AxisEvent event) 
  {
    if (event.getType() == AxisEvent.RANGE_CHANGE || event.getType() == AxisEvent.VISIBLE_RANGE_CHANGE)
    {
      DataSource src = chart.getDataSource();
      DataSet[] sets = src.getDataSets();
      int reduction = 1;
      for (DataSet set : sets)
      {
        if (set instanceof AbstractDataSet)
        {
          int r = ((AbstractDataSet)set).calculateReduction(chart.getXAxis().getVisibleRange());
          if (r > reduction) reduction = r;
        }
      }
    }
  }
  
}
