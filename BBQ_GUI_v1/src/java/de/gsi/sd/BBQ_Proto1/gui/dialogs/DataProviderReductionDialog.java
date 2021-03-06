/*****************************************************************************
 *                                                                           *
 * FCT - Data provider reduction dialog                                      *
 *                                                                           *
 * modified: 2012-06-15 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.gui.dialogs;

import de.gsi.sd.common.controls.localizable.LocalizableButton;
import de.gsi.sd.BBQ_Proto1.data.provider.JAPCDataProvider;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("serial")
public class DataProviderReductionDialog extends JDialog {

  private JRadioButton fullDataButton;
  private JRadioButton partialDataButton;
  private JRadioButton reducedDataButton;
  private SpinnerNumberModel reductionSpinnerModel;
  private int reduction;
  private boolean accepted = false;
  
  public DataProviderReductionDialog(JFrame frame, int reduction)
  {
    super(frame,"Data provider reduction...",true);
    this.reduction = reduction;  
    initComponents();
    pack();
    setLocationRelativeTo(frame);
  }
  
  public boolean isAccepted() 
  {
    return accepted;
  }

  public int getReduction()
  {
    if (reducedDataButton.isSelected()) return reductionSpinnerModel.getNumber().intValue();
    if (partialDataButton.isSelected()) return JAPCDataProvider.NO_REDUCTION_PARTIAL;
    return JAPCDataProvider.NO_REDUCTION;
  }
  
  private void initComponents()
  {
    setLayout(new BorderLayout());
    {
      Insets insets = new Insets(2,2,2,2);
      JPanel panel = new JPanel(new GridBagLayout());
      panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
      add(panel,BorderLayout.CENTER);
      ButtonGroup grp = new ButtonGroup();
      int row = 0;
      {
        reducedDataButton = new JRadioButton("reduce by factor: ");
        grp.add(reducedDataButton);
        panel.add(reducedDataButton,new GridBagConstraints(0,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        reductionSpinnerModel = new SpinnerNumberModel(reduction,reduction,100,1);
        JSpinner spin = new JSpinner(reductionSpinnerModel);
        panel.add(spin,new GridBagConstraints(1,row,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        row++;
      }
      {
        partialDataButton = new JRadioButton(String.format("get first %d samples only",JAPCDataProvider.MAX_SAMPLELENGTH));
        grp.add(partialDataButton);
        panel.add(partialDataButton,new GridBagConstraints(0,row,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        row++;
      }
      {
        fullDataButton = new JRadioButton("Try to get all data");
        grp.add(fullDataButton);
        panel.add(fullDataButton,new GridBagConstraints(0,row,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        row++;
      }
      partialDataButton.setSelected(true);
    }
    {
      GridLayout layout = new GridLayout(1,1);
      layout.setColumns(1);
      layout.setHgap(5);
      layout.setVgap(5);
      JPanel panel = new JPanel(layout);
      panel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
      add(panel,BorderLayout.SOUTH);
      {
        panel.add(new JPanel());
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
  }
 
  
}
