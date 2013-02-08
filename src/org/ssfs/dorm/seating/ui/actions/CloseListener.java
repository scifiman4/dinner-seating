
package org.ssfs.dorm.seating.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

/**
 * This class will help you take control of the frame closing process by
 * providing a WindowListener with some common features that might be used
 * when closing an application. That is you can:
 * <ul>
 * <li>display an application closing confirmation message which will give the
 * user a chance to cancel the close request
 * <li>invoke a provide Action. This action might be used to save the state of
 * the application for the next time it is used.
 * </ul>
 * 
 * This class will invoke the setDefaultCloseOperation(...) method with an
 * appropriate value. The default will be set to EXIT_ON_CLOSE. The default
 * can be changed by invoking the setDisoseOnClose() method with "true", in
 * which case DISPOSE_ON_CLOSE will be used. This value will be set to
 * DO_NOTHING_ON_CLOSE if the user does not confirm the application closing
 * message. Finally, the close action is free to set this property to any
 * value. <br>
 * <br>
 * Code from
 * <a href="http://tips4java.wordpress.com/2009/05/01/closing-an-application/">
 * here</a>.
 * 
 * @author <a href="http://tips4java.wordpress.com/">Rob Camick</a>
 */
public class CloseListener extends WindowAdapter {

	/** The close action. */
	private Action closeAction;

	/** The dispose on close. */
	private boolean disposeOnClose = false;

	/** The message. */
	private String message;

	/** The title. */
	private String title;

	/**
	 * Specify an Action to be invoked upon closing the application.
	 * 
	 * @param closeAction
	 *            the Action to be invoked upon closing the application
	 */
	public CloseListener(Action closeAction) {
		this(null, null, closeAction);
		message = "Are you sure you want to exit?";
		title = "Exit confirmation";
	}

	/**
	 * Display a confirmation message upon closing the application.
	 * 
	 * @param message
	 *            the confirmation message
	 * @param title
	 *            the text used for the title bar of the dialog
	 */
	public CloseListener(String message, String title) {
		this(message, title, null);
	}

	/**
	 * Display a confirmation message upon closing the application and
	 * and invoke a closing action.
	 * 
	 * @param message
	 *            the confirmation message
	 * @param title
	 *            the text used for the title bar of the dialog
	 * @param closeAction
	 *            the Action to be invoked upon closing the application
	 */
	public CloseListener(String message, String title, Action closeAction) {
		this.message = message;
		this.title = title;
		this.closeAction = closeAction;
	}

	/**
	 * Set the default close operation to be dispose on close. Typically would
	 * be used in a multi frame application. This way only when the last frame
	 * is closed will the JVM be shut down.
	 * 
	 * @param disposeOnClose
	 *            true for DISPOSE_ON_CLOSE, false for EXIT_ON_CLOSE
	 */
	public void setDisposeOnClose(boolean disposeOnClose) {
		this.disposeOnClose = disposeOnClose;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent e) {
		JFrame frame = (JFrame) e.getComponent();

		// Confirm with the user to close the frame

		if (!confirmWindowClosing(frame)) {
			frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			return;
		}

		// Set the close option. Setting it here allows the close action to
		// prevent the window from closing by resetting the close option to
		// DO_NOTHING_ON_CLOSE.

		if (disposeOnClose) {
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		} else {
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

		// Confirmation received, so do the exit processing

		if (closeAction != null) {
			ActionEvent ae = new ActionEvent(frame,
					ActionEvent.ACTION_PERFORMED, "");
			closeAction.actionPerformed(ae);
		}
	}

	/**
	 * Confirm window closing.
	 * 
	 * @param frame
	 *            the frame
	 * @return true, if successful
	 */
	private boolean confirmWindowClosing(JFrame frame) {
		if (message == null) {
			return true;
		}

		int result = JOptionPane.showConfirmDialog(frame, message, title,
				JOptionPane.YES_NO_OPTION);

		return (result == JOptionPane.YES_OPTION);
	}

}
