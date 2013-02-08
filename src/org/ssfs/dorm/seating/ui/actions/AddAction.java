
package org.ssfs.dorm.seating.ui.actions;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import org.ssfs.dorm.seating.domain.Person;
import org.ssfs.dorm.seating.domain.Table;

/**
 * The Class AddAction.
 */
public class AddAction extends AbstractAction {

	/** The is table. */
	private boolean isTable;

	/** The model. */
	private DefaultTableModel model;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new adds the action.
	 * 
	 * @param model
	 *            the model
	 * @param isTable
	 *            the true if this is an action for a table
	 */
	public AddAction(DefaultTableModel model, boolean isTable) {
		this.model = model;
		this.isTable = isTable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
	 * )
	 */
	public void actionPerformed(ActionEvent e) {
		JPanel panel = new JPanel(new GridBagLayout());

		JTextField name = new JTextField(10);

		JFormattedTextField size = new JFormattedTextField();

		size.setValue(isTable ? new Integer(1) : new String());
		size.setColumns(10);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;

		gbc.gridy = 0;
		panel.add(new JLabel("Name:", SwingConstants.RIGHT), gbc);

		gbc.gridy = 1;
		panel.add(new JLabel((isTable ? "Size" : "Nationality"),
				SwingConstants.RIGHT), gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(name, gbc);

		gbc.gridy = 1;
		panel.add(size, gbc);

		int result = JOptionPane.showConfirmDialog(null, panel, "Add",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			if (!name.getText().isEmpty() && !size.getText().isEmpty()) {
				if (isTable) {
					new Table(name.getText(), Integer.parseInt(size.getText()));
				} else {
					new Person(name.getText(), size.getText());
				}
				model.addRow(new Object[] { name.getText(), size.getText() });
			}
		}
	}

}
