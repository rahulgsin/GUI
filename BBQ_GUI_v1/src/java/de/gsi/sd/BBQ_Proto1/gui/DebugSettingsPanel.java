/*****************************************************************************
 *                                                                           *
 * FCT - Debug settings panel                                                *
 *                                                                           *
 * modified: 2012-06-14 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.gui;

import de.gsi.sd.common.controls.SDPanel;
import de.gsi.sd.common.controls.Updatable;
import de.gsi.sd.common.controls.localizable.LocalizableButton;
import de.gsi.sd.BBQ_Proto1.data.FCTDebugSettings;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
@Updatable
public class DebugSettingsPanel extends SDPanel {

  private JCheckBox debugOutput;
  private JCheckBox echoConsole;
  private JTextField serverName;
  private JTextField serverPort;

  public DebugSettingsPanel()
  {
    super();
    initComponents(true);
  }

  public DebugSettingsPanel(boolean buttons)
  {
    super();
    initComponents(buttons);
  }

  public void updateData(FCTDebugSettings settings)
  {
    debugOutput.setSelected(settings.isDebugOutput());
    echoConsole.setSelected(settings.isEchoConsole());
    serverName.setText(settings.getServerName());
    serverPort.setText(String.valueOf(settings.getServerPort()));
  }

  @Override
  public void get(Object data)
  {
    if (data instanceof FCTDebugSettings) get((FCTDebugSettings)data);
  }

  private void get(FCTDebugSettings settings)
  {
    settings.setDebugOutput(debugOutput.isSelected());
    settings.setEchoConsole(echoConsole.isSelected());
    settings.setServerName(serverName.getText());
    settings.setServerPort(Short.parseShort(serverPort.getText()));
  }

  private void initComponents(boolean buttons)
  {
    setLayout(new GridBagLayout());
    Insets insets = new Insets(2,2,2,2);
    int topRow = 0;
    {
      JPanel panel = new JPanel(new GridBagLayout());
      add(panel,new GridBagConstraints(1,topRow++,1,1,1.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,insets,0,0));
      panel.setBorder(BorderFactory.createTitledBorder("Debugging"));
      int row = 1;
      {
        debugOutput = new JCheckBox("enable debug output");
        panel.add(debugOutput,new GridBagConstraints(2,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        row++;
      }
      {
        echoConsole = new JCheckBox("echo to console");
        panel.add(echoConsole,new GridBagConstraints(2,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        row++;
      }
    }
    {
      JPanel panel = new JPanel(new GridBagLayout());
      add(panel,new GridBagConstraints(1,topRow++,1,1,1.0,0.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,insets,0,0));
      panel.setBorder(BorderFactory.createTitledBorder("Logging"));
      int row = 1;
      {
        panel.add(new JLabel("Server host name:"),new GridBagConstraints(0,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        serverName = new JTextField(20);
        panel.add(serverName,new GridBagConstraints(1,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        row++;
      }
      {
        panel.add(new JLabel("Server port:"),new GridBagConstraints(0,row,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0));
        serverPort = new JTextField(20);
        panel.add(serverPort,new GridBagConstraints(1,row,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,insets,0,0));
        row++;
      }
    }
    {
      JPanel fillerPanel = new JPanel(new GridBagLayout());
      add(fillerPanel,new GridBagConstraints(1,topRow++,1,1,0.0,1.0,GridBagConstraints.NORTH,GridBagConstraints.BOTH,insets,0,0));
    }
    if (buttons)
    {
      JPanel buttonPanel = new JPanel(new GridLayout(1,0));
      add(buttonPanel,new GridBagConstraints(1,topRow++,1,1,1.0,0.0,GridBagConstraints.SOUTHWEST,GridBagConstraints.HORIZONTAL,insets,0,0));
      {
        JButton setButton = new LocalizableButton("Button.Set");
        buttonPanel.add(setButton);
        setButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            fireEvent(FCTGUIEvent.ACTION_DEBUGSETTINGS_SET);
          }
        });
      }
      {
        JButton getButton = new LocalizableButton("Button.Get");
        buttonPanel.add(getButton);
        getButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            fireEvent(FCTGUIEvent.ACTION_DEBUGSETTINGS_GET);
          }
        });
      }
    }
  }

}
