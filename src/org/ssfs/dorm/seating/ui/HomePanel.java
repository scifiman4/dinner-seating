
package org.ssfs.dorm.seating.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * The Class HomePanel.
 */
public class HomePanel extends DinnerPanel implements ActionListener {

	/** The name. */
	public static final String name = "Home";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new home panel.
	 */
	public HomePanel() {
		super();

		remove(homeButton);

		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 3, 15, 0));

		p.add(makeButton("History"));
		p.add(makeButton("Configure"));
		p.add(makeButton("Exit"));

		JPanel panel = new JPanel(new GridLayout(2, 1, 0, 15));
		panel.add(makeButton("New Seats"));
		panel.add(p);

		add(panel, BorderLayout.CENTER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		AssignmentFrame frame = ((AssignmentFrame) SwingUtilities.getRoot(this));
		if (e.getActionCommand().equals("Exit")) {
			WindowEvent wev = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
			frame.dispatchEvent(wev);
		} else if (e.getActionCommand().equals("New Seats")) {
			frame.changeCard(SeatAssignmentPanel.name);
		} else if (e.getActionCommand().equals("History")) {
			frame.changeCard(HistoryPanel.name);
		} else if (e.getActionCommand().equals("Configure")) {
			frame.changeCard(ConfigurePanel.name);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ssfs.dorm.ui.DinnerPanel#getMyName()
	 */
	public String getMyName() {
		return name;
	}

	/**
	 * Makes a button.
	 * 
	 * @param text
	 *            the text of the button
	 * @return the j button
	 */
	private JButton makeButton(String text) {
		JButton button = new JButton(text);

		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.addActionListener(this);
		button.setActionCommand(text);
		button.setFont(new Font(Font.DIALOG, Font.PLAIN, 40));
		button.setFocusable(false);

		return button;
	}

}
