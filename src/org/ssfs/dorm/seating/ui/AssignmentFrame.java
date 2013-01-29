/*
 * File Name:
 * AssignmentFrame.java
 * 
 * Last Updated:
 * Jul 6, 2012 10:11:54 PM
 * 
 */

package org.ssfs.dorm.seating.ui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.ssfs.dorm.seating.ui.actions.CloseListener;
import org.ssfs.dorm.seating.ui.actions.ExitAction;

/**
 * This is the main frame / window for the GUI.
 * 
 * @author Matthew Denaburg '10
 */
public class AssignmentFrame extends JFrame {

	/** The main layout. */
	private final CardLayout mainLayout;

	/** The main panel. */
	private final JPanel mainPanel;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new assignment frame.
	 */
	public AssignmentFrame() {
		super();

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
		}

		mainLayout = new CardLayout();

		mainPanel = new JPanel(mainLayout);

		mainPanel.add(new ConfigurePanel(), ConfigurePanel.name);
		// TODO
		// mainPanel.add(new HistoryPanel(), HistoryPanel.name);
		// mainPanel.add(new HomePanel(), HomePanel.name);
		// mainPanel.add(new SeatAssignmentPanel(), SeatAssignmentPanel.name);

		setContentPane(mainPanel);

		Window window = (Window) SwingUtilities.getRoot(this);
		window.addWindowListener(new CloseListener(new ExitAction()));

		goHome();

		if (!Main.configExists) {
			JOptionPane.showMessageDialog(this,
					"<html><p>The previous configuration was not found.</p>"
							+ "<p>Please configure the system.</p></html>",
					"Configuration Not Found", JOptionPane.WARNING_MESSAGE);
			changeCard(ConfigurePanel.name);
		}

		setLocationByPlatform(true);
		setMinimumSize(new Dimension(850, 700));
		setPreferredSize(new Dimension(850, 700));
		setResizable(false);
		setTitle("Table Assignment Program");

		SwingUtilities.updateComponentTreeUI(this);
		pack();

		setVisible(true);
	}

	/**
	 * Change the visible panel.
	 * 
	 * @param identifier
	 *            the name of the panel
	 */
	public void changeCard(String identifier) {
		for (Component c : getContentPane().getComponents()) {
			if (c instanceof DinnerPanel) {
				DinnerPanel panel = (DinnerPanel) c;
				if (panel.getMyName().equals(identifier)) {
					if (panel instanceof HistoryPanel) {
						((HistoryPanel) panel).refresh();
					} else if (panel instanceof ConfigurePanel) {
						((ConfigurePanel) panel).stateChanged(null);
					}
					panel.changeLocationLabel(panel.getMyName());
					break;
				}
			}
		}
		mainLayout.show(mainPanel, identifier);
	}

	/**
	 * The method called when the home button is clicked.
	 */
	public void goHome() {
		// show the home screen
		mainLayout.show(mainPanel, HomePanel.name);
	}

}
