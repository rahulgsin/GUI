/*****************************************************************************
 *                                                                           *
 * FCT - Status panel                                                        *
 *                                                                           *
 * modified: 2012-06-14 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.gui;

import de.gsi.sd.common.controls.SDPanel;
import de.gsi.sd.common.controls.Updatable;
import de.gsi.sd.common.controls.localizable.LocalizableLabel;
import de.gsi.sd.common.oper.VirtAccInfo;
import de.gsi.sd.BBQ_Proto1.data.BBQStatus;
import de.gsi.sd.BBQ_Proto1.data.FESAData;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

@SuppressWarnings("serial")
@Updatable
public class StatusPanel extends SDPanel {

  private JLabel operInfoLabel = null;
  private JLabel vaccInfoLabel = null;
  private JLabel timeInfoLabel = null;
//  private JLabel cycleInfoLabel = null;
  private JLabel statusInfoLabel = null;
  private Color backgroundColor;
  private boolean statusHighlight = false;

  public StatusPanel() 
  {
    super();
    initComponents();
  }

  public void updateData(VirtAccInfo info)
  {
    String txt = "";
    txt += String.format("%d%s%d+  ",info.getIsotope(),info.getElement(),info.getCharge());
    txt += info.getPath() + "  ";
    txt += String.format("%.2f MeV/u ",info.getEnergy());
    operInfoLabel.setText(txt);
  }

  public void updateData(String info)
  {
    operInfoLabel.setText(info);
  }
  
  public void updateStatus(BBQStatus status)
  {
    statusInfoLabel.setText(String.format("%10s",status.getStatusString()));
    statusHighlight = !statusHighlight;
    if (statusHighlight)
    {
      if (status.isError())
        statusInfoLabel.setBackground(Color.RED);
      else
        statusInfoLabel.setBackground(Color.GREEN);
    }
    else
    {
      statusInfoLabel.setBackground(backgroundColor);
    }
  }

  public void updateData(FESAData data)
  {
//    vaccInfoLabel.setText(String.format("%2d",data.getAccelerator()));
    vaccInfoLabel.setText(data.getCycleId());
    timeInfoLabel.setText(data.getCycleStampString());
//    cycleInfoLabel.setText(String.format("%9d",data.getCycleCounter()));
  }



  @Override
  protected void visualFeedbackOn()
  {
    super.visualFeedbackOn();
    vaccInfoLabel.setBackground(Color.GREEN);
    timeInfoLabel.setBackground(Color.GREEN);
    operInfoLabel.setBackground(Color.GREEN);
  }

  @Override
  protected void visualFeedbackOff()
  {
    super.visualFeedbackOff();
    vaccInfoLabel.setBackground(backgroundColor);
    timeInfoLabel.setBackground(backgroundColor);
    operInfoLabel.setBackground(backgroundColor);
  }

  private void initComponents() 
  {
    setBorder(BorderFactory.createTitledBorder("Title.Status"));
    //      setPreferredSize(new java.awt.Dimension(757,60));
    setLayout(new GridBagLayout());
    Insets insets = new Insets(2,2,2,2);
    int row = 0;
    int col = 0;
    {
      operInfoLabel = new JLabel("               ");
      add(operInfoLabel,new GridBagConstraints(col,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
      operInfoLabel.setOpaque(true);
      operInfoLabel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
      col++;
    }
    {
      LocalizableLabel label = new LocalizableLabel("Label.Time");
      add(label,new GridBagConstraints(col,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
      col++;
      timeInfoLabel = new JLabel("                              ");
      add(timeInfoLabel,new GridBagConstraints(col,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
      timeInfoLabel.setOpaque(true);
      timeInfoLabel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
      col++;
    }
    {
      LocalizableLabel label = new LocalizableLabel("Label.VirtAcc");
      add(label,new GridBagConstraints(col,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
      col++;
      vaccInfoLabel = new JLabel("    ");
      add(vaccInfoLabel,new GridBagConstraints(col,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
      vaccInfoLabel.setOpaque(true);
      vaccInfoLabel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
      col++;
    }
//    {
//      LocalizableLabel label = new LocalizableLabel("Label.Cycle");
//      add(label,new GridBagConstraints(col,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
//      col++;
//      cycleInfoLabel = new JLabel();
//      add(cycleInfoLabel,new GridBagConstraints(col,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
//      cycleInfoLabel.setText("                ");
//      cycleInfoLabel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
//      col++;
//    }
    {
      add(new JPanel(),new GridBagConstraints(col,row,1,1,0.2,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
      col++;
    }
    {
      LocalizableLabel label = new LocalizableLabel("Label.Status");
      add(label,new GridBagConstraints(col,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
      col++;
      statusInfoLabel = new JLabel("No Events");
      add(statusInfoLabel,new GridBagConstraints(col,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
      statusInfoLabel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
      statusInfoLabel.setOpaque(true);
      col++;
    }
  }

}
