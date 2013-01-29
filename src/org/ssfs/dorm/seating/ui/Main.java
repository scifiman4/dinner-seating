/*
 * File Name:
 * Main.java
 * 
 * Last Updated:
 * Jul 6, 2012 10:11:55 PM
 * 
 */

package org.ssfs.dorm.seating.ui;

import javax.swing.SwingUtilities;

import org.ssfs.dorm.seating.domain.Config;

/**
 * The Class Main.
 * 
 * @author Matthew Denaburg '10
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
