package de.gsi.sd.BBQ_Proto1.gui.filter;

import de.gsi.sd.common.controls.localizable.LocalizableLabel;
import de.gsi.sd.common.controls.localizable.LocalizablePanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("serial")
public class MovingAverageFilterPanel extends LocalizablePanel {

  private SpinnerNumberModel lengthSpinnerModel = new SpinnerNumberModel(11,3,1023,2);
  
  public MovingAverageFilterPanel() 
  {
    super();
    initComponents();
  }

  public void setLength(int length)
  {
    lengthSpinnerModel.setValue(length);
  }
  
  public int getLength()
  {
    return lengthSpinnerModel.getNumber().intValue();
  }
  
  
  private void initComponents()
  {
    setLayout(new GridBagLayout());
    Insets insets = new Insets(2,2,2,2);
    int row = 0;
    {
      add(new LocalizableLabel("Label.Length"),new GridBagConstraints(0,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
      add(new JSpinner(lengthSpinnerModel),new GridBagConstraints(1,row,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
      row++;
    }
  }

}
