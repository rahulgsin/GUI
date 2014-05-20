/*****************************************************************************
 *                                                                           *
 * FCT - Expert status panel                                                 *
 *                                                                           *
 * modified: 2012-06-14 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.gui;

import de.gsi.sd.common.controls.BooleanIndicator;
import de.gsi.sd.common.controls.SDPanel;
import de.gsi.sd.common.controls.Updatable;
import de.gsi.sd.common.controls.localizable.LocalizableLabel;
import de.gsi.sd.common.controls.localizable.LocalizablePanel;
import de.gsi.sd.BBQ_Proto1.data.FCTStatus;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

@SuppressWarnings("serial")
@Updatable
public class ExpertStatusPanel extends SDPanel {

//  private JLabel temperatureInfoLabel;
  private JLabel cycleInfoLabel;
  private JLabel lostCycleInfoLabel;
  private JLabel ignoredCycleInfoLabel;
  private BooleanIndicator stoppedIndicator;
  private BooleanIndicator waitIndicator;
  private BooleanIndicator acquireIndicator;
  private BooleanIndicator readoutIndicator;
  private BooleanIndicator noEventIndicator;
  private BooleanIndicator fileOutIndicator;
  private JTextField fileOutRemainField;
  private JTextField filenameField;

  static private final Insets INSETS = new Insets(2,2,2,2);


  public ExpertStatusPanel()
  {
    super();
    initComponents();
  }

  public void updateStatus(FCTStatus status)
  {
//    temperatureInfoLabel.setText(String.format("%.1f",status.getTemperature()));
    stoppedIndicator.setOn(status.isStopped());
    waitIndicator.setOn(status.isWait());
    acquireIndicator.setOn(status.isAcquire());
    readoutIndicator.setOn(status.isReadout());
    noEventIndicator.setOn(status.isNoEvents());
    cycleInfoLabel.setText(String.format("%d",status.getCycleCounter()));
    lostCycleInfoLabel.setText(String.format("%d",status.getLostCycleCounter()));
    ignoredCycleInfoLabel.setText(String.format("%d",status.getIgnoredCycleCounter()));
    fileOutIndicator.setOn(status.isOutputInProgress());
    filenameField.setText(status.getFileName());
    fileOutRemainField.setText(String.format("%d",status.getFileOutRemain()));
  }





  private void initComponents()
  {
    setLayout(new GridBagLayout());
    int mainRow = 1;
//    {
//      JPanel panel = new JPanel(new GridBagLayout());
//      add(panel,new GridBagConstraints(1,mainRow,1,1,1.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,INSETS,0,0));
//      panel.setBorder(BorderFactory.createTitledBorder("ADC"));
//      int row = 1;
//      {
//        LocalizableLabel label = new LocalizableLabel("Label.Temperature");
//        panel.add(label,new GridBagConstraints(1,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,INSETS,0,0));
//        temperatureInfoLabel = new JLabel("99.99");
//        panel.add(temperatureInfoLabel,new GridBagConstraints(2,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,INSETS,0,0));
//        temperatureInfoLabel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
//        temperatureInfoLabel.setOpaque(true);
//        panel.add(new JLabel("°C"),new GridBagConstraints(3,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,INSETS,0,0));
//        row++;
//      }
//      mainRow++;
//    }
    {
      add(sourcePanel(),new GridBagConstraints(1,mainRow++,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,INSETS,0,0));
    }
    {
      add(fileOutputPanel(),new GridBagConstraints(1,mainRow++,1,1,1.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,INSETS,0,0));
    }
  }


  private JPanel fileOutputPanel()
  {
    JPanel panel = new LocalizablePanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createTitledBorder("Title.FileOutput"));
    int row = 0;
    {
      int col = 0;
      fileOutIndicator = new BooleanIndicator();
      panel.add(fileOutIndicator,new GridBagConstraints(col++,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,INSETS,0,0));
      fileOutIndicator.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
      LocalizableLabel label = new LocalizableLabel("Label.InProgress");
      panel.add(label,new GridBagConstraints(col++,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,INSETS,0,0));
      label = new LocalizableLabel("Label.SamplesRemain");
      panel.add(label,new GridBagConstraints(col++,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,INSETS,0,0));
      fileOutRemainField = new JTextField(10);
      panel.add(fileOutRemainField,new GridBagConstraints(col++,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,INSETS,0,0));
      fileOutRemainField.setEditable(false);
      row++;
    }
    {
      int col = 0;
//      LocalizableLabel label = new LocalizableLabel(language,"Label.CurrentFile");
      filenameField = new JTextField(20);
      panel.add(filenameField,new GridBagConstraints(col++,row,5,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,INSETS,0,0));
      filenameField.setEditable(false);
      row++;
    }
    return panel;
  }
  
  private JPanel sourcePanel()
  {
    JPanel panel = new LocalizablePanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createTitledBorder("Title.EventSource"));
    int row = 0;
    {
      LocalizableLabel label;
//      LocalizableLabel label = new LocalizableLabel(language,"Label.States");
//      panel.add(label,new GridBagConstraints(0,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,INSETS,0,0));
      int col = 0;
      label = new LocalizableLabel("Label.Stopped");
      panel.add(label,new GridBagConstraints(col++,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,INSETS,0,0));
      stoppedIndicator = new BooleanIndicator();
      panel.add(stoppedIndicator,new GridBagConstraints(col++,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,INSETS,0,0));
      stoppedIndicator.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
      label = new LocalizableLabel("Label.NoEvent");
      panel.add(label,new GridBagConstraints(col++,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,INSETS,0,0));
      noEventIndicator = new BooleanIndicator();
      panel.add(noEventIndicator,new GridBagConstraints(col++,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,INSETS,0,0));
      noEventIndicator.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
      row++;
      col = 0;
      label = new LocalizableLabel("Label.Wait");
      panel.add(label,new GridBagConstraints(col++,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,INSETS,0,0));
      waitIndicator = new BooleanIndicator();
      panel.add(waitIndicator,new GridBagConstraints(col++,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,INSETS,0,0));
      waitIndicator.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
      label = new LocalizableLabel("Label.Acquire");
      panel.add(label,new GridBagConstraints(col++,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,INSETS,0,0));
      acquireIndicator = new BooleanIndicator();
      panel.add(acquireIndicator,new GridBagConstraints(col++,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,INSETS,0,0));
      acquireIndicator.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
      label = new LocalizableLabel("Label.Readout");
      panel.add(label,new GridBagConstraints(col++,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,INSETS,0,0));
      readoutIndicator = new BooleanIndicator();
      panel.add(readoutIndicator,new GridBagConstraints(col++,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,INSETS,0,0));
      readoutIndicator.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
      row++;
    }
    {
      int col = 0;
      LocalizableLabel label = new LocalizableLabel("Label.Cycle");
      panel.add(label,new GridBagConstraints(col++,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,INSETS,0,0));
      cycleInfoLabel = new JLabel("0");
      panel.add(cycleInfoLabel,new GridBagConstraints(col++,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,INSETS,0,0));
      cycleInfoLabel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
      cycleInfoLabel.setOpaque(true);
//      row++;
//    }
//    {
      label = new LocalizableLabel("Label.CycleIgnored");
      panel.add(label,new GridBagConstraints(col++,row,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.NONE,INSETS,0,0));
      ignoredCycleInfoLabel = new JLabel("0");
      panel.add(ignoredCycleInfoLabel,new GridBagConstraints(col++,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,INSETS,0,0));
      ignoredCycleInfoLabel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
      ignoredCycleInfoLabel.setOpaque(true);
//      row++;
//    }
//    {
      label = new LocalizableLabel("Label.CycleLost");
      panel.add(label,new GridBagConstraints(col++,row,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.NONE,INSETS,0,0));
      lostCycleInfoLabel = new JLabel("0");
      panel.add(lostCycleInfoLabel,new GridBagConstraints(col++,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,INSETS,0,0));
      lostCycleInfoLabel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
      lostCycleInfoLabel.setOpaque(true);
      row++;
    }
    return panel;
  }
  
  
}
