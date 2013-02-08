
package org.ssfs.dorm.seating.ui.actions;

import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.ssfs.dorm.seating.domain.Config;

/**
 * This class will create and dispatch a WINDOW_CLOSING event to the active
 * frame. As a result a request to close the frame will be made and any
 * WindowListener that handles the windowClosing event will be executed.
 * Since clicking on the "Close" button of the frame or selecting the "Close"
 * option from the system menu also invoke the WindowListener, this will
 * provide a commen exit point for the application.<br>
 * <br>
 * Code from
 * <a href="http://tips4java.wordpress.com/2009/05/01/closing-an-application/">
 * here</a>.
 * 
 * @author <a href="http://tips4java.wordpress.com/">Rob Camick</a>
 */
public class ExitAction extends AbstractAction {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8801409785396597931L;

	/**
	 * Instantiates a new exit action.
	 */
	public ExitAction() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		// Find the active frame before creating and dispatching the event

		for (Frame frame : Frame.getFrames()) {
			if (!Config.saveConfiguration()) {
				JOptionPane.showMessageDialog(frame,
						"Could not save your settings.", "Save error",
						JOptionPane.ERROR_MESSAGE);
			}

			WindowEvent windowClosing = new WindowEvent(frame,
					WindowEvent.WINDOW_CLOSING);
			Toolkit.getDefaultToolkit().getSystemEventQueue()
					.postEvent(windowClosing);
		}
	}

}
