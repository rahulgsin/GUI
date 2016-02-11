package de.gsi.sd.BBQ_Proto1.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JDesktopPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.UIManager;
import java.awt.Rectangle;
import java.awt.GridBagLayout;
import javax.swing.JTabbedPane;
import java.awt.GridBagConstraints;
import javax.swing.JRadioButton;
import java.awt.Insets;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JCheckBox;

public class Windowpanel {

	private JFrame frmBbqGui;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Windowpanel window = new Windowpanel();
					window.frmBbqGui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Windowpanel() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBbqGui = new JFrame();
		frmBbqGui.setTitle("BBQ GUI");
		frmBbqGui.setBounds(100, 100, 688, 511);
		frmBbqGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JDesktopPane desktopPane = new JDesktopPane();
		frmBbqGui.getContentPane().add(desktopPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setBounds(12, 222, 257, 247);
		desktopPane.add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("New radio button");
		GridBagConstraints gbc_rdbtnNewRadioButton_1 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnNewRadioButton_1.gridx = 1;
		gbc_rdbtnNewRadioButton_1.gridy = 0;
		panel.add(rdbtnNewRadioButton_1, gbc_rdbtnNewRadioButton_1);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("New radio button");
		GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
		gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnNewRadioButton.gridx = 1;
		gbc_rdbtnNewRadioButton.gridy = 1;
		panel.add(rdbtnNewRadioButton, gbc_rdbtnNewRadioButton);
		
		JButton btnNewButton = new JButton("New button");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 2;
		panel.add(btnNewButton, gbc_btnNewButton);
		
		JSpinner spinner = new JSpinner();
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.gridx = 1;
		gbc_spinner.gridy = 4;
		panel.add(spinner, gbc_spinner);
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBounds(261, 209, -247, -169);
		desktopPane.add(layeredPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(4, 0, 680, 26);
		desktopPane.add(panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(281, 83, 403, 386);
		desktopPane.add(panel_2);
		
		JCheckBox chckbxSaveData = new JCheckBox("Save Data");
		panel_2.add(chckbxSaveData);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"FFT", "Capon", "APES"}));
		panel_2.add(comboBox);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(281, 30, 403, 52);
		desktopPane.add(panel_3);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(26, 38, 189, 172);
		desktopPane.add(tabbedPane);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_4, null);
		
		JPanel panel_5 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_5, null);
		
		JMenuBar menuBar = new JMenuBar();
		frmBbqGui.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("New menu");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmSet = new JMenuItem("Set");
		mnNewMenu.add(mntmSet);
		
		JCheckBoxMenuItem chckbxmntmStandardMode = new JCheckBoxMenuItem("Standard mode");
		mnNewMenu.add(chckbxmntmStandardMode);
		
		JMenuItem mntmGet = new JMenuItem("Get");
		mnNewMenu.add(mntmGet);
		
		JMenu mnAcquistionMode = new JMenu("Acquistion Mode");
		mnNewMenu.add(mnAcquistionMode);
		
		JMenuItem mntmStandardMode = new JMenuItem("Standard Mode");
		mnAcquistionMode.add(mntmStandardMode);
		
		JMenuItem mntmExpertMode = new JMenuItem("Expert Mode");
		mnAcquistionMode.add(mntmExpertMode);
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
