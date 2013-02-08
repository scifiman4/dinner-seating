
package org.ssfs.dorm.seating.ui.actions;

import java.awt.event.ActionEvent;

import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

import org.ssfs.dorm.seating.domain.Config;
import org.ssfs.dorm.seating.domain.DinnerObject;

/**
 * The Class LoadAction.
 */
@SuppressWarnings("javadoc")
public class LoadAction extends AbstractAction {

	protected boolean isTable;

	/** The model. */
	protected DefaultTableModel model;

	private static final long serialVersionUID = 1176842983709171855L;

	/**
	 * Instantiates a new load action.
	 * 
	 * @param model
	 *            the model
	 * @param isTable
	 *            the is table
	 */
	public LoadAction(DefaultTableModel model, boolean isTable) {
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
		JFileChooser fileChooser = new JFileChooser(".",
				FileSystemView.getFileSystemView());
		fileChooser.setMultiSelectionEnabled(false);

		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File f = fileChooser.getSelectedFile();

			if (isTable) {
				Config.importTables(f);
			} else {
				Config.importPeople(f);
			}
			for (DinnerObject obj : Config.getType(isTable)) {
				model.addRow(new Object[] { obj.getName(), obj.getSecondField() });
			}
		}
	}

}
