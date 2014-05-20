/*****************************************************************************
 *                                                                           *
 * FCT - Tabbed controls panel                                               *
 *                                                                           *
 * modified: 2012-04-02 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.gui;

import de.gsi.sd.common.controls.SDPanel;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class TabbedControlsPanel extends SDPanel {

  private JTabbedPane tabPane;
  
  public TabbedControlsPanel()
  {
    super();
    initComponents();
  }
  


  private void initComponents()
  {
    setLayout(new BorderLayout());
    tabPane = new JTabbedPane();
    add(tabPane,BorderLayout.CENTER);
    {
      JPanel panel = new SettingsPanel();
      tabPane.addTab("Settings",panel);
    }
    {
      JPanel panel = new ExpertSettingsPanel();
      tabPane.addTab("ExpertSettings",panel);
    }
    {
      JPanel panel = new DisplayPanel();
      tabPane.addTab("Display",panel);
    }
    {
//      JPanel panel = new ExpertStatusPanel(language);
//      tabPane.addTab("Status",panel);
    }
    {
//      JPanel panel = new AnalysisPanel(language);
//      tabPane.addTab("Analysis",panel);
    }
  }
  
}
