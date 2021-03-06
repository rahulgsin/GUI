/*****************************************************************************
 *                                                                           *
 * FCT - Add data filter dialog                                              *
 *                                                                           *
 * modified: 2012-04-02 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.gui.dialogs;

import de.gsi.sd.common.controls.localizable.LocalizableButton;
import de.gsi.sd.common.controls.localizable.LocalizablePanel;
import de.gsi.sd.common.language.Language;
import de.gsi.sd.BBQ_Proto1.data.filter.DataFilter;
import de.gsi.sd.BBQ_Proto1.data.filter.DataFilterOptions;
import de.gsi.sd.BBQ_Proto1.data.filter.FFTFilter;
import de.gsi.sd.BBQ_Proto1.data.filter.FFTFilter.FFTFilterOptions;
import de.gsi.sd.BBQ_Proto1.data.filter.MomentFilter;
import de.gsi.sd.BBQ_Proto1.data.filter.MovingAverageFilter;
import de.gsi.sd.BBQ_Proto1.data.filter.MovingAverageFilter.MovingAverageFilterOptions;
import de.gsi.sd.BBQ_Proto1.data.filter.StackFilter;
import de.gsi.sd.BBQ_Proto1.gui.filter.FFTFilterPanel;
import de.gsi.sd.BBQ_Proto1.gui.filter.MovingAverageFilterPanel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class AddFilterDialog extends JDialog {

  private JComboBox filterBox;
  private JComboBox srcBox;
  private JTextField destField;
  private LocalizablePanel configurationPanel;
  private JPanel filterConfigurationPanel;
  private boolean accepted = false;
  
  public AddFilterDialog(JFrame frame, String[] dataSets)
  {
    super(frame,"Add filter...",true);
    initComponents(dataSets);
    pack();
    setLocationRelativeTo(frame);
  }
  
  public boolean isAccepted() 
  {
    return accepted;
  }

  public DataFilter getFilter()
  {
    String id = filterBox.getSelectedItem().toString();
    if (id.equals(MovingAverageFilter.ID))
    {
      return new MovingAverageFilter();
    }
    else if (id.equals(FFTFilter.ID))
    {
      return new FFTFilter();
    }
    else if (id.equals(StackFilter.ID))
    {
      return new StackFilter();
    }
    else if (id.equals(MomentFilter.ID))
    {
      return new MomentFilter();
    }
    return null;
  }
  
  public DataFilterOptions getFilterOptions()
  {
    DataFilterOptions options;
    String id = filterBox.getSelectedItem().toString();
    if (id.equals(MovingAverageFilter.ID))
    {
      options = new MovingAverageFilter.MovingAverageFilterOptions(srcBox.getSelectedItem().toString(),destField.getText());
      int length = ((MovingAverageFilterPanel)filterConfigurationPanel).getLength();
      ((MovingAverageFilterOptions)options).setLength(length);
    }
    else if (id.equals(FFTFilter.ID))
    {
      options = new FFTFilter.FFTFilterOptions(srcBox.getSelectedItem().toString(),destField.getText());
      boolean log = ((FFTFilterPanel)filterConfigurationPanel).isLogScaling();
      ((FFTFilterOptions)options).setLogScaling(log);
    }
    else
    {
      options = new DataFilterOptions(srcBox.getSelectedItem().toString(),destField.getText());
    }
    return options;
  }
  
  private void initComponents(String[] dataSets)
  {
    Language language = Language.getInstance();
    setLayout(new BorderLayout());
    {
      Insets insets = new Insets(2,2,2,2);
      JPanel panel = new JPanel(new GridBagLayout());
      add(panel,BorderLayout.CENTER);
      int row = 0;
      {
        panel.add(new JLabel(language.getString("Label.Filter")),new GridBagConstraints(0,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        String[] filterList = new String[4];
        filterList[0] = MovingAverageFilter.ID;
        filterList[1] = FFTFilter.ID;
        filterList[2] = StackFilter.ID;
        filterList[3] = MomentFilter.ID;
        filterBox = new JComboBox(filterList);
        filterBox.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            updateConfigurationPanel();
          }
        });
        panel.add(filterBox,new GridBagConstraints(1,row,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        row++;
      }
      {
        configurationPanel = new LocalizablePanel(new BorderLayout());
        configurationPanel.setBorder(BorderFactory.createTitledBorder("Title.Configuration"));
        panel.add(configurationPanel,new GridBagConstraints(1,row,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        row++;
      }
      {
        panel.add(new JLabel(language.getString("Label.SourceDataSet")),new GridBagConstraints(0,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        srcBox = new JComboBox(dataSets);
        panel.add(srcBox,new GridBagConstraints(1,row,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        row++;
      }
      {
        panel.add(new JLabel(language.getString("Label.DestDataSet")),new GridBagConstraints(0,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        destField = new JTextField(20);
        panel.add(destField,new GridBagConstraints(1,row,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        row++;
      }
    }
    {
      GridLayout layout = new GridLayout(1,1);
      layout.setColumns(1);
      layout.setHgap(5);
      layout.setVgap(5);
      JPanel panel = new JPanel(layout);
      add(panel,BorderLayout.SOUTH);
      {
        JPanel jPanel2 = new JPanel();
        panel.add(jPanel2);
      }
      {
        JButton okButton = new LocalizableButton("Button.Ok");
        panel.add(okButton);
        okButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            accepted = true;
            close();
          }
        });
        getRootPane().setDefaultButton(okButton);
      }
      {
        JButton cancelButton = new LocalizableButton("Button.Cancel");
        panel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            accepted = false;
            close();
          }
        });
      }
    }
    updateConfigurationPanel();
  }
 
  private void updateConfigurationPanel()
  {
    if (filterConfigurationPanel != null)
    {
      configurationPanel.remove(filterConfigurationPanel);
    }
    filterConfigurationPanel = null;
    String id = filterBox.getSelectedItem().toString();
    if (id.equals(MovingAverageFilter.ID))
    {
      filterConfigurationPanel = new MovingAverageFilterPanel();
      configurationPanel.add(filterConfigurationPanel,BorderLayout.CENTER);
    }    
    else if (id.equals(FFTFilter.ID))
    {
      filterConfigurationPanel = new FFTFilterPanel();
      configurationPanel.add(filterConfigurationPanel,BorderLayout.CENTER);
    }    
    validate();
  }
  
  private void close()
  {
    setVisible(false);
    ((Window)getParent()).toFront();
    getParent().requestFocus();
  }
  
}
