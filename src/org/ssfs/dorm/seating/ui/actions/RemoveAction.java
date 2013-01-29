/*
 * File Name:
 * RemoveAction.java
 * 
 * Last Updated:
 * Jul 30, 2012 11:40:07 AM
 * 
 */

package org.ssfs.dorm.seating.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.ssfs.dorm.seating.domain.Config;
import org.ssfs.dorm.seating.domain.DinnerObject;

/**
 * The Class RemoveAction.
 * 
 * @author Matthew Denaburg '10
 */
public class RemoveAction extends AbstractAction {

	/** The is table. */
	protected boolean isTable;

	/** The model. */
	protected DefaultTableModel model;

	/** The table. */
	private JTable table;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new adds the action.
	 * 
	 * @param table
	 *            the table
	 * @param model
	 *            the model
	 * @param isTable
	 *            the true if this is an action for a table
	 */
	public RemoveAction(JTable table, DefaultTableModel model, boolean isTable) {
		this.model = model;
		this.isTable = isTable;
		this.table = table;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
	 * )
	 */
	public void actionPerformed(ActionEvent e) {
		int[] rows = table.getSelectedRows();
		boolean removeAll = false;
		while (rows.length > 0) {
			int row = rows[0];
			DinnerObject obj;
			String name = (String) table.getValueAt(row, 0);
			if (isTable) {
				obj = Config.getTableByName(name);
			} else {
				obj = Config.getPersonByName(name);
			}
			if (obj == null) {
				break;
			}
			if (!removeAll) {
				String[] options;
				if (rows.length > 1) {
					options = new String[] { "Yes", "No", "Remove all" };
				} else {
					options = new String[] { "Yes", "No" };
				}
				int result = JOptionPane
						.showOptionDialog(null,
								"Are you sure you wish to delete " + obj + "?",
								"Confirm removal",
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[0]);
				removeAll = result == JOptionPane.CANCEL_OPTION;

				if (result == JOptionPane.CLOSED_OPTION) {
					break;
				} else if (result != JOptionPane.NO_OPTION) {
					remove(obj, row);
				}
				rows = popFirst(rows);
			} else {
				remove(obj, row);
				rows = popFirst(rows);
			}
		}
	}

	/**
	 * Removes the.
	 * 
	 * @param objToRemove
	 *            the object to remove
	 * @param rowToRemove
	 *            the row to remove
	 */
	private void remove(DinnerObject objToRemove, int rowToRemove) {
		if (Config.removeObject(objToRemove) != null) {
			model.removeRow(rowToRemove);
		} else {
			JOptionPane.showMessageDialog(null, "Could not remove "
					+ objToRemove + ".", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Pop first.
	 * 
	 * @param array
	 *            the array
	 * @return the int[]
	 */
	private static int[] popFirst(int[] array) {
		int[] toReturn = new int[array.length - 1];
		for (int i = 0; i < toReturn.length;) {
			toReturn[i] = array[++i] - 1;
		}
		return toReturn;
	}

}
