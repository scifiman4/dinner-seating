/*
 * File Name:
 * ListDialog.java
 * 
 * Last Updated:
 * Jul 6, 2012 10:11:55 PM
 * 
 */

package org.ssfs.dorm.seating.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.ssfs.dorm.seating.domain.Person;

/**
 * Use this modal dialogues to let the user choose one string from a long
 * list. See ListDialogRunner.java for an example of using ListDialog.
 * The basics:
 * 
 * <pre>
 * String[] choices = { 'A', 'long', 'array', 'of', 'strings' };
 * 
 * String selectedName = ListDialog.showDialog(componentInControllingFrame,
 * 		locatorComponent, 'A description of the list:', 'Dialog Title',
 * 		choices, choices[0]);
 * </pre>
 * 
 * @author Matthew Denaburg '10
 */
public class ListDialog extends JDialog implements ActionListener,
		ListSelectionListener {

	/** The list. */
	private final JList list;

	/** The set button. */
	private final JButton setButton;

	/** The dialogues. */
	private static ListDialog dialog;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The values. */
	private static Set<Person> values;

	/**
	 * Instantiates a new list dialogues.
	 * 
	 * @param frame
	 *            the frame
	 * @param locationComp
	 *            the location comp
	 * @param labelText
	 *            the label text
	 * @param title
	 *            the title
	 * @param data
	 *            the data
	 * @param initialValue
	 *            the initial value
	 * @param longValue
	 *            the long value
	 */
	private ListDialog(Frame frame, Component locationComp, String labelText,
			String title, Object[] data, String initialValue, String longValue) {
		super(frame, title, true);

		// Create and initialize the buttons.
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);

		setButton = new JButton("Set");
		setButton.setActionCommand("Set");
		setButton.addActionListener(this);
		getRootPane().setDefaultButton(setButton);
		setButton.setEnabled(false);

		values = new TreeSet<Person>();

		// main part of the dialogues
		list = new JList(data);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		if (longValue != null) {
			list.setPrototypeCellValue(longValue); // get extra space
		}
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		list.setFixedCellWidth(100);
		list.addListSelectionListener(this);

		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(400, 350));
		listScroller.setAlignmentX(LEFT_ALIGNMENT);

		// Create a container so that we can add a title around
		// the scroll pane. Can't add a title directly to the
		// scroll pane because its background would be white.
		// Lay out the label and scroll pane from top to bottom.
		JPanel listPane = new JPanel();
		listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
		JLabel label = new JLabel(labelText);
		label.setLabelFor(list);
		listPane.add(label);
		listPane.add(Box.createRigidArea(new Dimension(0, 20)));
		listPane.add(listScroller);
		listPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Lay out the buttons from left to right.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(cancelButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(setButton);

		// Put everything together, using the content pane's BorderLayout.
		Container contentPane = getContentPane();
		contentPane.add(listPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);

		// Initialize value.
		pack();
		setLocationRelativeTo(locationComp);
	}

	// Handle clicks on the Set and Cancel buttons.
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if ("Set".equals(e.getActionCommand())) {
			for (Object obj : list.getSelectedValuesList()) {
				values.add((Person) obj);
			}
		}
		setVisible(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
	 * .ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		int count = values.size() > 0 ? 1 : 0;
		setButton.setEnabled(list.getSelectedIndices().length > count);
	}

	/**
	 * Set up and show the dialogues. The first Component argument determines
	 * which
	 * frame the dialogues depends on; it should be a component in the
	 * dialogues's
	 * controlling frame. The second Component argument should be null if you
	 * want the dialogues to come up with its left corner in the center of the
	 * screen; otherwise, it should be the component on top of which the
	 * dialogues should appear.
	 * 
	 * @param frameComp
	 *            the frame comp
	 * @param locationComp
	 *            the location comp
	 * @param labelText
	 *            the label text
	 * @param title
	 *            the title
	 * @param possibleValues
	 *            the possible value
	 * @param initialValue
	 *            the initial value
	 * @param longValue
	 *            the long value
	 * @return the string
	 */
	public static Set<Person> showDialog(Component frameComp,
			Component locationComp, String labelText, String title,
			Object[] possibleValues, String initialValue, String longValue) {

		Frame frame = JOptionPane.getFrameForComponent(frameComp);
		dialog = new ListDialog(frame, locationComp, labelText, title,
				possibleValues, initialValue, longValue);
		dialog.setVisible(true);

		return values;
	}

}
