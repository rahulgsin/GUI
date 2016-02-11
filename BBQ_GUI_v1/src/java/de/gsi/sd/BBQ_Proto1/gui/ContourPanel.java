package de.gsi.sd.BBQ_Proto1.gui;
/* $Id: ContourDemo.java,v 1.7 2008-02-20 16:40:45 gkruk Exp $
 * 
 * $Date: 2008-02-20 16:40:45 $ $Revision: 1.7 $ $Author: gkruk $
 * 
 * Copyright CERN, All Rights Reserved.
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.gsi.sd.common.controls.SDPanel;
import de.gsi.sd.common.controls.localizable.LocalizableLabel;
import cern.jdve.Chart;
import cern.jdve.ChartInteractor;
import cern.jdve.ChartRenderer;
import cern.jdve.data.DataSet;
import cern.jdve.data.DefaultDataSet3D;
import cern.jdve.graphic.RainbowPalette;
import cern.jdve.interactor.DataPickerInteractor;
import cern.jdve.renderer.ContourChartRenderer;

/**
 * The class presents usage of {@link cern.jdve.renderer.ContourChartRenderer} and of {@link cern.jdve.data.DataSet3D}.
 * It displays beam image as color-coded map. There is also a possibility to modify a coefficient of color-coding
 * function.
 * 
 * @version $Id: ContourDemo.java,v 1.7 2008-02-20 16:40:45 gkruk Exp $
 */
public class ContourPanel extends SDPanel {
    private Chart chart;
    ContourChartRenderer contourChartRenderer;// = new ContourChartRenderer();
    
    public ContourPanel() {
    	super();
        initComponents();
    }

    private void initComponents() {
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());
        contourChartRenderer = new ContourChartRenderer();
        add(createChart(), BorderLayout.CENTER);
        add(createSlider(), BorderLayout.SOUTH);
        
    }
    
    public Chart getChart()
    {
      return chart;
    }
    
    public void setDataSet(DefaultDataSet3D ds)
    {
        contourChartRenderer.setDataSet(ds);
    }

    private Chart createChart() {
        this.chart = new Chart();

        // Set rendering type and data set
         chart.setRenderingType(ChartRenderer.CONTOUR);
        // chart.setDataSet(createDataSet());

        // Or create a renderer and add it to the chart
         RainbowPalette rainbowPalette = new RainbowPalette(0, 5);
         ContourChartRenderer contourChartRenderer = new ContourChartRenderer(rainbowPalette);
        
       // contourChartRenderer.getColorPalette().setValueRange(0, 1);
      //  contourChartRenderer.setColorPaletteAutoRange(false);
        
        contourChartRenderer.setColorPaletteAutoRange(true);

        this.chart.addRenderer(contourChartRenderer);

        // Add data picker interactor
        DataPickerInteractor dataPicker = new DataPickerInteractor();

        // We don't need cursor coordinates - all pixels represent data points
        // So point coordinates are sufficient
        dataPicker.setCursorCoordPainted(true);

        // We have only one data set - so we don't need its name
        dataPicker.setDisplayDataSetName(true);
        this.chart.addInteractor(dataPicker);
        this.chart.addInteractor(ChartInteractor.ZOOM);

        // Set substeps for both scales
    //    this.chart.getXScale().setStepUnit(null, new Double(1));
   //     this.chart.getYScale().setStepUnit(null, new Double(1));

        // Set titles for both scales
        this.chart.setXScaleTitle("Time [ms]");
        this.chart.setYScaleTitle("Fractional Tune");

        return this.chart;
    }

    /**
     * Reads image from text file.
     */
   /* public static DataSet createDataSet() {
        try {
            //BufferedReader reader = new BufferedReader(new InputStreamReader(ContourDemo.class
            //        .getResourceAsStream("image.txt")));
        	InputStream inputStream = new FileInputStream("image.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            String nbOfX = reader.readLine();
            String[] x = reader.readLine().split(" ");
            String nbOfY = reader.readLine();
            String[] y = reader.readLine().split(" ");
            String nbOfXY = reader.readLine();
            String[] z = reader.readLine().split(" ");
            reader.close();

            double[] xValues = new double[x.length];
            for (int i = 0; i < x.length; i++) {
                xValues[i] = Double.parseDouble(x[i]);
            }

            double[] yValues = new double[y.length];
            for (int i = 0; i < y.length; i++) {
                yValues[i] = Double.parseDouble(y[i]);
            }

            double[] zValues = new double[z.length];
            for (int i = 0; i < z.length; i++) {
                zValues[i] = Double.parseDouble(z[i]);
            }

            // create and return data set.
            DefaultDataSet3D ds = new DefaultDataSet3D("Image");
            ds.set(xValues, yValues, zValues, false, false);
            return ds;

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Could not read image file:\n" + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }*/

    /**
     * Creates JSlider which can be used to modify exponent of the color-coding function.
     */
    private JComponent createSlider() {
        JPanel pnl = new JPanel(new BorderLayout());

        JSlider slider = new JSlider(1, 19, 10);
        slider.setExtent(1);
        slider.setSnapToTicks(true);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                JSlider source = (JSlider) evt.getSource();

                if (!source.getValueIsAdjusting()) {
                    int exp = source.getValue();

                    ContourChartRenderer renderer = (ContourChartRenderer) ContourPanel.this.chart.getRenderer(0);
                    RainbowPalette palette = (RainbowPalette) renderer.getColorPalette();
                    if (exp > 10) {
                        palette.setExp(exp - 9);
                    } else {
                        palette.setExp((double) exp / 10);
                    }

                    // This repaints the chart
                    renderer.setColorPalette(palette);
                }
            }
        });
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        Hashtable labels = new Hashtable();
        for (int i = 1; i < 10; i++) {
            labels.put(new Integer(i), new JLabel("0." + i));
        }
        for (int i = 10; i < 20; i++) {
            labels.put(new Integer(i), new JLabel("" + (i - 9)));
        }
        slider.setLabelTable(labels);

        pnl.add(slider, BorderLayout.CENTER);
        pnl.setBorder(new TitledBorder("Colour coding"));
        return pnl;
    }

  /*  public static void main(String[] args) {
        JFrame f = new JFrame("ContourDemo [exponential coding]");
        f.getContentPane().add(new ContourPanel());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }*/
}