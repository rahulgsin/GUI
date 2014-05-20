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

@SuppressWarnings("serial")
@Updatable
public class GUIStatusPanel extends SDPanel {

  private JTextField fecReductionLabel;

  static private final Insets INSETS = new Insets(2,2,2,2);


  public GUIStatusPanel()
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
    int mainRow = 1;
    {
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
    }
  }


  
}
