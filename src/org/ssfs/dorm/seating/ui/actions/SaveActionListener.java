/*
 * File Name:
 * SaveActionListener.java
 * 
 * Last Updated:
 * Jul 20, 2012 2:12:25 PM
 * 
 */

package org.ssfs.dorm.seating.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.ssfs.dorm.seating.domain.Config;

/**
 * The listener interface for receiving saveAction events.
 * The class that is interested in processing a saveAction
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addSaveActionListener<code> method. When
 * the saveAction event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ActionEvent
 */
public class SaveActionListener implements ActionListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("people")) {
			Config.savePeople();
		} else if (e.getActionCommand().equals("tables")) {
			Config.saveTables();
		} else {
			return;
		}
		JOptionPane.showMessageDialog(
				SwingUtilities.getRoot((Component) e.getSource()),
				"Save successful.");
	}

}
