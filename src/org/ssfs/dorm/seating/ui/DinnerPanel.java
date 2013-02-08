
package org.ssfs.dorm.seating.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * This is the base panel for all of the main display panels in the GUI.
 */
public abstract class DinnerPanel extends JPanel implements ActionListener {

	/** The home button. */
	protected final JButton homeButton;

	/** The location label. */
	private final JLabel locationLabel;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new dinner panel.
	 */
	protected DinnerPanel() {
		super(new BorderLayout(0, 5));

		locationLabel = new JLabel();

		JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);

		JPanel north = new JPanel(new BorderLayout());
		north.add(locationLabel, BorderLayout.CENTER);
		north.add(sep, BorderLayout.SOUTH);

		homeButton = new JButton("Home");
		homeButton.addActionListener(this);
		homeButton.setFocusable(false);

		add(north, BorderLayout.NORTH);
		add(homeButton, BorderLayout.SOUTH);

		setBorder(BorderFactory.createEmptyBorder(12, 30, 15, 30));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		AssignmentFrame frame = (AssignmentFrame) SwingUtilities
				.getRoot(homeButton);
		frame.goHome();
	}

	/**
	 * Change location label.
	 * 
	 * @param newLoc
	 *            the new loc
	 */
	public void changeLocationLabel(String newLoc) {
		locationLabel.setText("Dinner Seating > " + newLoc);
	}

	/**
	 * Gets the name of this panel.
	 * 
	 * @return the name of this panel.
	 */
	public abstract String getMyName();

}
