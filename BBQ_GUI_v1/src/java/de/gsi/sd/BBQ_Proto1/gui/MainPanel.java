/*****************************************************************************
 *                                                                           *
 * BBQ GUI - Main panel                                               *
 *                                                                           *
 * modified: 2013-11-13 Rahul Singh                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.gui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;





import de.gsi.sd.common.controls.AbstractMainPanel;
import de.gsi.sd.common.controls.PrintPages;
import de.gsi.sd.common.controls.Updatable;
import de.gsi.sd.common.controls.localizable.LocalizableButton;
import de.gsi.sd.common.language.Language;
import de.gsi.sd.BBQ_Proto1.BBQ_GUIApplication;
//import de.gsi.sd.fct.gui.ExpertStatusPanel;
//import de.gsi.sd.fct.gui.FCTGUIEvent;
import de.gsi.sd.BBQ_Proto1.gui.ResultPanel;
import de.gsi.sd.BBQ_Proto1.gui.StatusPanel;
import de.gsi.sd.BBQ_Proto1.data.BBQStatus;
import de.gsi.sd.BBQ_Proto1.gui.GraphPanel;
//import de.gsi.sd.BBQ_Proto1.gui.StatusPanel;
import de.gsi.sd.BBQ_Proto1.gui.TabbedControlsPanel;
//import de.gsi.sd.fct.data.FCTStatus; /* I am using FCT GUI code*/






import java.io.File;

import org.apache.log4j.Logger;
/**
 * The MainPanel is the central GUI panel of the application. It contains
 * all relevant GUI elements. 
 */
@SuppressWarnings("serial")
@Updatable
public class MainPanel extends AbstractMainPanel {

  private StatusPanel statusPanel;
  private GraphPanel dataPanel;
  private TabbedControlsPanel tabPanel;
  private JPanel buttonPanel;
  private JPanel controlsPanel;
  private JButton subscribeButton;
  private JButton startButton;

  /* Name of the subscribe action */
  static public final String ACTION_SUBSCRIBE   = "Subscribe";
  /** Name of the unsubscribe action */
  static public final String ACTION_UNSUBSCRIBE = "Unsubscribe"; 
  @SuppressWarnings("unused")
  static private Logger logger = Logger.getLogger(MainPanel.class);

  /**
   * Constructor for the main panel
   */
  public MainPanel()
  {
    super();
    initGUI();
  }

   public void updateStatus(BBQStatus status)
  {
    setRunning(!status.isStopped());
  }



  @Override
  public PrintPages getPrintPages()
  {
    return dataPanel.getPrintPages();
  }
  
  @Override
  public boolean export(File file, String format)
  {
    return dataPanel.export(file,format);
  }
  
  public boolean isControlsVisible()
  {
    return controlsPanel.isVisible();
  }
  
  public void setControlsVisible(boolean flag)
  {
    controlsPanel.setVisible(flag);
  }
  
  public boolean isStatusVisible()
  {
    return statusPanel.isVisible();
  }
  
  public void setStatusVisible(boolean flag)
  {
    statusPanel.setVisible(flag);
  }
  
  public boolean isSubscribed()
  {
	    if (subscribeButton.getText() == "Button.Subscribe")
	    {
	      return true;
	    }
	    else
	    {
	    return false;
	    }

  }
  
  
  public void setSubscribed(boolean flag)
  {
    if (flag)
    {
      subscribeButton.setText("Button.Unsubscribe");
    }
    else
    {
      subscribeButton.setText("Button.Subscribe");
    }
  }

  public void setRunning(boolean flag)
  {
    if (flag)
    {
      startButton.setText("Button.Stop");
    }
    else
    {
      startButton.setText("Button.Start");
    }
  }

  
  
  private void initGUI() 
  {
    Language language = Language.getInstance();
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(1024,800));
    {
      JPanel panel = new JPanel(new GridBagLayout());
      {
        statusPanel = new StatusPanel();
        panel.add(statusPanel,new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0));
      }
      this.add(panel,BorderLayout.NORTH);
    }
    {
      dataPanel = new GraphPanel();
      add(dataPanel,BorderLayout.CENTER);
    }
    {
      controlsPanel = new JPanel(new GridBagLayout());
      add(controlsPanel,BorderLayout.WEST);
      int row = 0;
      {
        tabPanel = new TabbedControlsPanel();
        controlsPanel.add(tabPanel,new GridBagConstraints(0,row,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0));
        row++;
      }
      {
          controlsPanel.add(new ResultPanel(),new GridBagConstraints(0,row,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0));
          row++;
        }
      {
        JPanel fillerPanel = new JPanel(new GridBagLayout());
        controlsPanel.add(fillerPanel,new GridBagConstraints(0,row,1,1,0.0,1.0,GridBagConstraints.NORTH,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0));
        row++;
      }
      {
        buttonPanel = new JPanel(new GridLayout(1,0));
        controlsPanel.add(buttonPanel,new GridBagConstraints(0,row,1,1,1.0,0.0,GridBagConstraints.SOUTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0));
        {
          startButton = new LocalizableButton("Button.Start");
          buttonPanel.add(startButton);
          startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
              fireEvent(BBQGUIEvent.ACTION_MEASUREMENT);
            }
          });
        }
        {
          subscribeButton = new LocalizableButton(ACTION_SUBSCRIBE);
          buttonPanel.add(subscribeButton);
          subscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
            	JButton b = (JButton)evt.getSource();
                if (evt.getActionCommand().equalsIgnoreCase(ACTION_SUBSCRIBE))
                {
                b.setText(ACTION_UNSUBSCRIBE);
                fireEvent(BBQGUIEvent.ACTION_SUBSCRIBE);
                BBQ_GUIApplication.getLogger().info("Mainpanel :: Subscription start");
                System.out.println("Mainpanel :: Subscription start");
                }
                else
                {
                b.setText(ACTION_SUBSCRIBE);
                fireEvent(BBQGUIEvent.ACTION_UNSUBSCRIBE);
                BBQ_GUIApplication.getLogger().info("Mainpanel :: Subscription end");
                System.out.println("Mainpanel :: Subscription end");
                }
            }
          });
        }
        row++;
      }
    }
  }

  /*
/*@SuppressWarnings("serial")
 public class MainPanel extends JPanel {*/
}
