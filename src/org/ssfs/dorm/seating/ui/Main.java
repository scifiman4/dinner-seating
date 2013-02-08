
package org.ssfs.dorm.seating.ui;

import javax.swing.SwingUtilities;

import org.ssfs.dorm.seating.domain.Config;

/**
 * The Class Main.
 */
public class Main {

	/** The config exists. */
	public static boolean configExists;

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		configExists = Config.loadConfiguration();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new AssignmentFrame();
			}
		});
	}

}
