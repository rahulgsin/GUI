/*****************************************************************************
 *                                                                           *
 * FCT - Display control panel                                               *
 *                                                                           *
 * modified: 2012-06-14 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.gui;

import cern.jdve.Chart;
import cern.jdve.utils.DataRange;
import de.gsi.sd.BBQ_Proto1.data.FESAData;
import de.gsi.sd.common.controls.SDPanel;
import de.gsi.sd.common.controls.Updatable;
import de.gsi.sd.common.controls.localizable.LocalizableButton;
import de.gsi.sd.common.controls.localizable.LocalizableCheckBox;
import de.gsi.sd.common.controls.localizable.LocalizableLabel;
import de.gsi.sd.common.controls.localizable.LocalizablePanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
@Updatable
public class DisplayPanel extends SDPanel {

  private JList dataSetList;
  private JList filterList;
  private SpinnerNumberModel rowSpinnerModel = new SpinnerNumberModel(4,1,10,1);
  private SpinnerNumberModel colSpinnerModel = new SpinnerNumberModel(1,1,3,1);
  private JTextField xmin;
  private JTextField xmax;
  private JTextField ymin;
  private JTextField ymax;
  private JCheckBox xautoBox;
  private JCheckBox yautoBox;

  public DisplayPanel()
  {
    super();
    initComponents();
  }

  public void updateData(FESAData data)
  {
    dataSetList.setListData(data.getDataSetList());
    filterList.setListData(data.getFilterList());
  }
  
  public void updateData(Chart chart)
  {
    DataRange range = chart.getXAxis().getTransformedVisibleRange();
    xmin.setText(String.format("%.6f",range.getMin()));
    xmax.setText(String.format("%.6f",range.getMax()));
    xautoBox.setSelected(chart.getXAxis().isAutoVisibleRange());
    range = chart.getYAxis().getTransformedVisibleRange();
    ymin.setText(String.format("%.6f",range.getMin()));
    ymax.setText(String.format("%.6f",range.getMax()));
    yautoBox.setSelected(chart.getYAxis().isAutoVisibleRange());
  }

  private void initComponents()
  {
    setLayout(new GridBagLayout());
    Insets insets = new Insets(2,2,2,2);
    int mainRow = 1;
    {
      JPanel panel = new LocalizablePanel(new GridBagLayout());
      panel.setBorder(BorderFactory.createTitledBorder("Title.Graphs"));
      add(panel,new GridBagConstraints(0,mainRow,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
      int row = 0;
      {
        panel.add(new LocalizableLabel("Label.Rows"),new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        panel.add(new JSpinner(rowSpinnerModel),new GridBagConstraints(2,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        rowSpinnerModel.addChangeListener(new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent e) {
            graphLayoutChanged();
          }
        });
        row++;
      }
      {
        panel.add(new LocalizableLabel("Label.Cols"),new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        panel.add(new JSpinner(colSpinnerModel),new GridBagConstraints(2,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        colSpinnerModel.addChangeListener(new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent e) {
            graphLayoutChanged();
          }
        });
        row++;
      }
      {
        panel.add(new LocalizableLabel("Label.XRange"),new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        xmin = new JTextField(10);
        panel.add(xmin,new GridBagConstraints(2,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        xmin.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            DataRange range = new DataRange(Double.parseDouble(xmin.getText()),Double.parseDouble(xmax.getText()));
            fireEvent(FCTGUIEvent.ACTION_DISPLAY_SETXRANGE,range);
          }
        });
        row++;
        xmax = new JTextField(10);
        panel.add(xmax,new GridBagConstraints(2,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        xmax.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            DataRange range = new DataRange(Double.parseDouble(xmin.getText()),Double.parseDouble(xmax.getText()));
            fireEvent(FCTGUIEvent.ACTION_DISPLAY_SETXRANGE,range);
          }
        });
        row++;
        xautoBox = new LocalizableCheckBox("Button.Autoscale");
        panel.add(xautoBox,new GridBagConstraints(2,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        xautoBox.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            boolean s = ((JCheckBox)e.getSource()).isSelected();
            fireEvent(FCTGUIEvent.ACTION_DISPLAY_AUTOXRANGE,s);
          }
        });
        row++;
        LocalizableCheckBox sync = new LocalizableCheckBox("Button.Sync");
        panel.add(sync,new GridBagConstraints(2,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        sync.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            boolean s = ((JCheckBox)e.getSource()).isSelected();
            fireEvent(FCTGUIEvent.ACTION_DISPLAY_SYNC,s);
          }
        });
        row++;
      }
      {
        panel.add(new LocalizableLabel("Label.YRange"),new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        ymin = new JTextField(10);
        panel.add(ymin,new GridBagConstraints(2,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        ymin.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            DataRange range = new DataRange(Double.parseDouble(ymin.getText()),Double.parseDouble(ymax.getText()));
            fireEvent(FCTGUIEvent.ACTION_DISPLAY_SETYRANGE,range);
          }
        });
        row++;
        ymax = new JTextField(10);
        panel.add(ymax,new GridBagConstraints(2,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        ymax.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            DataRange range = new DataRange(Double.parseDouble(ymin.getText()),Double.parseDouble(ymax.getText()));
            fireEvent(FCTGUIEvent.ACTION_DISPLAY_SETYRANGE,range);
          }
        });
        row++;
        yautoBox = new LocalizableCheckBox("Button.Autoscale");
        panel.add(yautoBox,new GridBagConstraints(2,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        yautoBox.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            boolean s = ((JCheckBox)e.getSource()).isSelected();
            fireEvent(FCTGUIEvent.ACTION_DISPLAY_AUTOYRANGE,s);
          }
        });
        row++;
      }
      {
        JButton button = new LocalizableButton("Button.Clear");
        panel.add(button,new GridBagConstraints(2,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        button.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            fireEvent(FCTGUIEvent.ACTION_DISPLAY_CLEAR);
          }
        });
        row++;
      }
      {
        panel.add(new JPanel(),new GridBagConstraints(1,row,1,1,0,1,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,insets,0,0));
        row++;
      }
      {
        dataSetList = new JList();
        panel.add(new JScrollPane(dataSetList),new GridBagConstraints(0,0,1,row,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        dataSetList.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e)
          {
            dataSetListClicked(e);
          }
        });
      }
      mainRow++;
    }
    {
      JPanel panel = new LocalizablePanel(new GridBagLayout());
      panel.setBorder(BorderFactory.createTitledBorder("Title.Filters"));
      add(panel,new GridBagConstraints(0,mainRow,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
      int row = 0;
      {
        JButton button = new LocalizableButton("Button.AddFilter");
        panel.add(button,new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        button.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            fireEvent(FCTGUIEvent.ACTION_FILTER_ADD);
          }
        });
        row++;
      }
      {
        JButton button = new LocalizableButton("Button.RemoveFilter");
        panel.add(button,new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        button.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            int item = filterList.getSelectedIndex();
            if (item >= 0) fireEvent(FCTGUIEvent.ACTION_FILTER_REMOVE,new Integer(item));
          }
        });
        row++;
      }
      {
        JButton button = new LocalizableButton("Button.EditFilter");
        panel.add(button,new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        button.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            int item = filterList.getSelectedIndex();
            if (item >= 0) fireEvent(FCTGUIEvent.ACTION_FILTER_EDIT,new Integer(item));
          }
        });
        row++;
      }
      {
        panel.add(new JPanel(),new GridBagConstraints(1,row,1,1,0,1,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,insets,0,0));
        row++;
      }
      {
        filterList = new JList();
        panel.add(new JScrollPane(filterList),new GridBagConstraints(0,0,1,row,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        filterList.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e)
          {
            filterListClicked(e);
          }
        });
      }
    }
  }

  private void dataSetListClicked(MouseEvent e)
  {
    if (e.getClickCount() == 2)
    {
      int index = dataSetList.locationToIndex(e.getPoint());
      if (index >= 0) fireEvent(FCTGUIEvent.ACTION_DISPLAY_GRAPH,dataSetList.getModel().getElementAt(index).toString());
    }
  }

  private void filterListClicked(MouseEvent e)
  {
  }

  private void graphLayoutChanged()
  {
    int[] a = new int[2];
    a[0] = rowSpinnerModel.getNumber().intValue();
    a[1] = colSpinnerModel.getNumber().intValue();
    fireEvent(FCTGUIEvent.ACTION_DISPLAY_LAYOUT,a);
  }
}
