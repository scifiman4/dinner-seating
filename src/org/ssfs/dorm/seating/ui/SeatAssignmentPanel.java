/*
w * File Name:
 * SeatAssignmentPanel.java
 * 
 * Last Updated:
 * Jul 10, 2012 2:02:39 PM
 * 
 */

package org.ssfs.dorm.seating.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.ssfs.dorm.seating.domain.Config;
import org.ssfs.dorm.seating.domain.SeatAssigner;

/**
 * The Class SeatAssignmentPanel.
 * 
 * @author Matthew Denaburg '10
 */
public class SeatAssignmentPanel extends DinnerPanel {

	/** The assigned seats. */
	private boolean assignedSeats;

	/** The Constant name. */
	public static final String name = "Seat Assignment";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new seat assignment panel.
	 */
	public SeatAssignmentPanel() {
		super();

		assignedSeats = false;

		final SeatAssignmentPanel myself = this;

		final JEditorPane textPane = new JEditorPane();
		textPane.setEditable(false);
		textPane.setText("");
		textPane.setMargin(new Insets(4, 3, 4, 3));

		final JButton assign = new JButton("Get new Assignments");
		final JButton confirm = new JButton("Save Assignments");
		final JButton copy = new JButton("Copy to Clipboard");

		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Config.saveConfiguration();

				assignedSeats = true;
				confirm.setEnabled(false);
			}
		});
		confirm.setFocusable(false);
		confirm.setEnabled(false);

		copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPane.selectAll();
				textPane.copy();
				textPane.setSelectionStart(0);
				textPane.setSelectionEnd(0);
				JOptionPane.showMessageDialog(myself, "Copied!");
			}
		});
		copy.setFocusable(false);
		copy.setEnabled(false);

		assign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = "";
				AssignmentFrame frame = (AssignmentFrame) SwingUtilities
						.getRoot(assign);
				boolean problem = false;
				if (Config.getType(false).size() > Config
						.getTotalAvailableSpaces()) {
					message = "There are " + Config.getType(false).size()
							+ ", and only " + Config.getTotalAvailableSpaces()
							+ ". Please add enough seats for every person.";
					problem = true;
				} else if (Config.getType(false).size() == 0) {
					message = "There are no people. Please add some to "
							+ "the database.";
					problem = true;
				} else if (Config.getType(true).size() == 0) {
					message = "There are no tables. Please add some to "
							+ "the database.";
					problem = true;
				}
				if (problem) {
					JOptionPane.showMessageDialog(myself, message, "Error",
							JOptionPane.ERROR_MESSAGE);
					frame.changeCard(ConfigurePanel.name);
					return;
				}

				int choice = JOptionPane.YES_OPTION;
				if (assignedSeats) {
					choice = JOptionPane.showConfirmDialog(frame,
							"You have already assigned seats. Do you wish to "
									+ "do so again?", "Confirm Re-Assignment",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
				}
				if (choice == JOptionPane.YES_OPTION) {
					SeatAssigner a = new SeatAssigner();
					a.assign();

					textPane.setText(WriteFiles.makeAssignmentString()
							.toString());

					confirm.setEnabled(true);
					copy.setEnabled(true);
				}
			}
		});
		assign.setFocusable(false);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 0));
		buttonPanel.add(assign);
		buttonPanel.add(confirm);
		buttonPanel.add(copy);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(textPane), BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		add(panel, BorderLayout.CENTER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ssfs.dorm.ui.DinnerPanel#getMyName()
	 */
	public String getMyName() {
		return name;
	}

}
