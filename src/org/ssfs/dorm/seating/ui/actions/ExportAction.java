/*
 * File Name:
 * ExportAction.java
 * 
 * Last Updated:
 * Jul 30, 2012 11:35:33 AM
 * 
 */

package org.ssfs.dorm.seating.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import org.ssfs.dorm.seating.domain.Config;

/**
 * The Class ExportAction.
 * 
 * @author Matthew Denaburg '10
 */
public class ExportAction extends AbstractAction {

	/** The is table. */
	private boolean isTable;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new export action.
	 * 
	 * @param isTable
	 *            the is table
	 */
	public ExportAction(boolean isTable) {
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
		FileSystemView fileSystem = FileSystemView.getFileSystemView();
		JFileChooser fileChooser = new JFileChooser(".", fileSystem);
		fileChooser.setMultiSelectionEnabled(false);

		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			Config.export(isTable, fileChooser.getSelectedFile());
		}
	}

}
