package de.gsi.sd.BBQ_Proto1.gui.filter;

import de.gsi.sd.common.controls.localizable.LocalizableCheckBox;
import de.gsi.sd.common.controls.localizable.LocalizablePanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class FFTFilterPanel extends LocalizablePanel {

  private JCheckBox logScaleBox;
  
  public FFTFilterPanel() 
  {
    super();
    initComponents();
  }

  public void setLogScaling(boolean log)
  {
    logScaleBox.setSelected(log);
  }
  
  public boolean isLogScaling()
  {
    return logScaleBox.isSelected();
  }
  
  
  private void initComponents()
  {
    setLayout(new GridBagLayout());
    Insets insets = new Insets(2,2,2,2);
    int row = 0;
    {
      logScaleBox = new LocalizableCheckBox("Button.LogScale");
      add(logScaleBox,new GridBagConstraints(0,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
      row++;
    }
  }

}
