/*****************************************************************************
 *                                                                           *
 * FCT - Analysis panel                                                      *
 *                                                                           *
 * modified: 2012-04-02 Harald Braeuning                                     *
 *                                                                           *
 ****************************************************************************/

package de.gsi.sd.BBQ_Proto1.gui;

import de.gsi.sd.common.controls.SDPanel;
import de.gsi.sd.common.controls.Updatable;
import de.gsi.sd.common.controls.localizable.LocalizableButton;
import de.gsi.sd.BBQ_Proto1.data.SimpleStatistics;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
@Updatable
public class AnalysisPanel extends SDPanel {

  private JTextArea editorPane = null;
  private JButton statButton = null;
  private JPopupMenu contextMenu = null;
  private JMenuItem menuClear = null;
  private JMenuItem menuCopy = null;
  private JMenuItem menuCut = null;
  private JMenuItem menuPaste = null;

  public AnalysisPanel()
  {
    super();
    initComponents();
  }


  public void updateData(SimpleStatistics stat) 
  {
    editorPane.append(stat.toString());
  }



  private void initComponents()
  {
    setLayout(new GridBagLayout());
    Insets insets = new Insets(2,2,2,2);
    int mainRow = 1;
    {
      editorPane = new JTextArea();
      editorPane.setFont(new Font(Font.MONOSPACED,Font.PLAIN,12));
      editorPane.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent evt)
        {
          mousePressedInEditor(evt);
        }
      });
      JScrollPane scrollPane = new JScrollPane(editorPane);
      add(scrollPane,new GridBagConstraints(0,mainRow,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,insets,0,0));
      mainRow++;
    }
    {
      GridLayout layout = new GridLayout(0,2);
      JPanel panel = new JPanel(layout);
      add(panel,new GridBagConstraints(0,mainRow,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,insets,0,0));
      {
        statButton = new LocalizableButton("Button.Statistics");
        panel.add(statButton);
        statButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            fireEvent(FCTGUIEvent.ACTION_ANALYSIS_STATISTICS);
          }
        });
      }
    }
    createContextMenu();
  }

  private void createContextMenu()
  {
    contextMenu = new JPopupMenu();
    menuCopy = new JMenuItem("Copy");
    menuCopy.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
        copyText();
      }
    });
    contextMenu.add(menuCopy);
    menuCut = new JMenuItem("Cut");
    menuCut.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
        cutText();
      }
    });
    contextMenu.add(menuCut);
    menuPaste = new JMenuItem("Paste");
    menuPaste.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
        pasteText();
      }
    });
    contextMenu.add(menuPaste);
    contextMenu.addSeparator();
    menuClear = new JMenuItem("Clear");
    menuClear.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
        clearEditorPane();
      }
    });
    contextMenu.add(menuClear);
  }

  private void mousePressedInEditor(MouseEvent evt)
  {
    if (evt.isPopupTrigger())
    {
      if (contextMenu != null) 
      {
        contextMenu.show(this,evt.getX(),evt.getY());
      }
    }
  }

  private void clearEditorPane()
  {
    editorPane.setText("");
  }

  private void copyText()
  {
    editorPane.copy();
  }

  private void cutText()
  {
    editorPane.copy();
  }

  private void pasteText()
  {
    editorPane.copy();
  }

}
