/*****************************************************************************
 *                                                                           *
 * FCT - Edit data filter dialog                                             *
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
import de.gsi.sd.BBQ_Proto1.data.filter.FFTFilter.FFTFilterOptions;
import de.gsi.sd.BBQ_Proto1.data.filter.MovingAverageFilter.MovingAverageFilterOptions;
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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class EditFilterDialog extends JDialog {

  private DataFilterOptions options;
  private LocalizablePanel configurationPanel;
  private JPanel filterConfigurationPanel;
  private boolean accepted = false;
  
  public EditFilterDialog(JFrame frame, DataFilter filter, DataFilterOptions options)
  {
    super(frame,"Edit filter...",true);
    this.options = options;
    initComponents(filter,options);
    pack();
    setLocationRelativeTo(frame);
  }
  
  public boolean isAccepted() 
  {
    return accepted;
  }
  
  public DataFilterOptions getOptions()
  {
    if (options instanceof MovingAverageFilterOptions && filterConfigurationPanel instanceof MovingAverageFilterPanel)
    {
      ((MovingAverageFilterOptions)options).setLength(((MovingAverageFilterPanel)filterConfigurationPanel).getLength());
    }
    else if (options instanceof FFTFilterOptions && filterConfigurationPanel instanceof FFTFilterPanel)
    {
      ((FFTFilterOptions)options).setLogScaling(((FFTFilterPanel)filterConfigurationPanel).isLogScaling());
    }
    return options;
  }

  private void initComponents(DataFilter filter, DataFilterOptions options)
  {
    Language language = Language.getInstance();
    setLayout(new BorderLayout());
    {
      Insets insets = new Insets(2,2,2,2);
      JPanel panel = new JPanel(new GridBagLayout());
      add(panel,BorderLayout.CENTER);
      int row = 0;
      {
        panel.add(new JLabel(filter.getName()),new GridBagConstraints(0,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        row++;
      }
      {
        configurationPanel = new LocalizablePanel(new BorderLayout());
        configurationPanel.setBorder(BorderFactory.createTitledBorder("Title.Configuration"));
        panel.add(configurationPanel,new GridBagConstraints(0,row,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
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
            setVisible(false);
            ((Window)getParent()).toFront();
            getParent().requestFocus();
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
            setVisible(false);
            ((Window)getParent()).toFront();
            getParent().requestFocus();
          }
        });
      }
    }
    updateConfigurationPanel(options);
  }
 
  private void updateConfigurationPanel(DataFilterOptions options)
  {
    if (filterConfigurationPanel != null)
    {
      configurationPanel.remove(filterConfigurationPanel);
    }
    filterConfigurationPanel = null;
    if (options instanceof MovingAverageFilterOptions)
    {
      MovingAverageFilterPanel panel = new MovingAverageFilterPanel();
      filterConfigurationPanel = panel;
      panel.setLength(((MovingAverageFilterOptions)options).getLength());
    }    
    else if (options instanceof FFTFilterOptions)
    {
      FFTFilterPanel panel = new FFTFilterPanel();
      filterConfigurationPanel = panel;
      panel.setLogScaling(((FFTFilterOptions)options).isLogScaling());
    }
    if (filterConfigurationPanel != null ) configurationPanel.add(filterConfigurationPanel,BorderLayout.CENTER);
  }
  
  
  
}
