/*
 * File Name:
 * RemoveEnabler.java
 * 
 * Last Updated:
 * Aug 2, 2012 1:43:36 PM
 * 
 */

package org.ssfs.dorm.seating.ui.actions;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * The Class RemoveEnabler.
 * 
 * @author Matthew Denaburg '10
 */
public class RemoveEnabler implements ListSelectionListener {

	/** The flag. */
	private JTable table;

	/** The button. */
	private JButton button;

	/**
	 * Instantiates a new removes the enabler.
	 * 
	 * @param table
	 *            the table
	 * @param button
	 *            the button
	 */
	public RemoveEnabler(JTable table, JButton button) {
		this.table = table;
		this.button = button;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
	 * .ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		button.setEnabled(!table.getSelectionModel().isSelectionEmpty());
	}

}
