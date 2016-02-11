/*****************************************************************************
 *                                                                           *
 * FCT - GUI status panel                                                    *
 *                                                                           *
 * modified: 2012-06-14 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.gui;

import de.gsi.sd.common.controls.SDPanel;
import de.gsi.sd.common.controls.Updatable;
import de.gsi.sd.common.controls.localizable.LocalizableLabel;
import de.gsi.sd.common.controls.localizable.LocalizablePanel;
import de.gsi.sd.common.language.Language;
import de.gsi.sd.BBQ_Proto1.data.GUIStatus;
import de.gsi.sd.BBQ_Proto1.data.provider.JAPCDataProvider;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;

@SuppressWarnings("serial")
@Updatable
public class ResultPanel extends SDPanel {

  private JTextField fecReductionLabel;
  private JTextField tunexField;
  private JTextField tuneyField;
  private JTextField couplingField;
  private JTextField deltaField;

  static private final Insets insets = new Insets(2,2,2,2);


  public ResultPanel()
  {
    super();
    initComponents();
  }

  public void updateStatus(GUIStatus status)
  {
    if (status.getFecDataReduction() == JAPCDataProvider.NO_REDUCTION)
    {
      String txt = Language.getInstance().getString("Label.AllData");
      fecReductionLabel.setText(txt);
      fecReductionLabel.setBackground(Color.GREEN);
    }
    else if (status.getFecDataReduction() == JAPCDataProvider.NO_REDUCTION_PARTIAL)
    {
      String fmt = Language.getInstance().getString("Format.FirstN");
      fecReductionLabel.setText(String.format(fmt,JAPCDataProvider.MAX_SAMPLELENGTH));
      fecReductionLabel.setBackground(Color.RED);
    }
    else
    {
      String fmt = Language.getInstance().getString("Format.EveryN");
      fecReductionLabel.setText(String.format(fmt,status.getFecDataReduction()));
      fecReductionLabel.setBackground(Color.RED);
    }
  }





  private void initComponents()
  {
    setLayout(new GridBagLayout());
    int row = 1;
/*    {
      JPanel panel = new LocalizablePanel(new GridBagLayout());
      add(panel,new GridBagConstraints(1,mainRow,1,1,1.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,INSETS,0,0));
      panel.setBorder(BorderFactory.createTitledBorder("Title.GUIStatus"));
      int row = 1;
      {
        LocalizableLabel label = new LocalizableLabel("Label.FECReduction");
        panel.add(label,new GridBagConstraints(0,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,INSETS,0,0));
        fecReductionLabel = new JTextField(20);
        panel.add(fecReductionLabel,new GridBagConstraints(1,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,INSETS,0,0));
        fecReductionLabel.setEditable(false);
      }
      mainRow++;
    }*/
    {
        JPanel panel = new LocalizablePanel(new GridBagLayout());
        add(panel,new GridBagConstraints(0,row++,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
        panel.setBorder(BorderFactory.createTitledBorder("Results"));
        int srow = 0;
    /*    {
          panel.add(new JLabel("Acquired Time Sequence:"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
          valueField = new JTextField(5);
          valueField.setEditable(false);
          panel.add(valueField,new GridBagConstraints(1,srow,3,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
          srow++;
        }
     */
        {
          panel.add(new JLabel("Tune X (Qx):"),new GridBagConstraints(0,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
          tunexField = new JTextField(5);
          tunexField.setEditable(false);
          panel.add(tunexField,new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
          panel.add(new JLabel("Tune Y (Qy):"),new GridBagConstraints(2,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
          tuneyField = new JTextField(5);
          tuneyField.setEditable(false);
          panel.add(tuneyField,new GridBagConstraints(3,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
          srow++;
        }
        {
          panel.add(new JLabel("Coupling (C-):"),new GridBagConstraints(0,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
          couplingField = new JTextField(5);
          couplingField.setEditable(false);
          panel.add(couplingField,new GridBagConstraints(1,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
          panel.add(new JLabel("Delta:"),new GridBagConstraints(2,srow,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
          deltaField = new JTextField(5);
          deltaField.setEditable(false);
          panel.add(deltaField,new GridBagConstraints(3,srow,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,insets,0,0));
          srow++;
        }
      
    
  }
    
  }
  
}

