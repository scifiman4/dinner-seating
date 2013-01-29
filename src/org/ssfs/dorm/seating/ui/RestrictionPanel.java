/*
 * File Name:
 * RestrictionPanel.java
 * 
 * Last Updated:
 * Jul 6, 2012 10:11:55 PM
 * 
 */

package org.ssfs.dorm.seating.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import org.ssfs.dorm.seating.domain.Person;
import org.ssfs.dorm.seating.ui.actions.SaveActionListener;

/**
 * The Class RestrictionPanel.
 * 
 * @author Matthew Denaburg '10
 */
public class RestrictionPanel extends JPanel implements ItemListener {

	/** The view. */
	private final JPanel view;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new restriction panel.
	 */
	public RestrictionPanel() {
		super(new BorderLayout(0, 2));

		view = new JPanel(new GridBagLayout());

		JViewport viewport = new JViewport();
		viewport.add(view);

		JScrollPane scroll = new JScrollPane(viewport);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBorder(BorderFactory.createEmptyBorder());

		for (List<Person> people : Config.getRestrictionGroups()) {
			addRestrictions(people);
		}

		Integer[] tableSizes = new Integer[16];
		for (int i = 0; i < 16; i++) {
			tableSizes[i] = i + 10;
		}

		JComboBox sizes = new JComboBox(tableSizes);
		sizes.setFocusable(false);
		sizes.setEditable(false);
		sizes.addItemListener(this);
		sizes.setSelectedIndex(Config.getMaxTableAssignmentCount() - 10);

		JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
		sep.setForeground(new Color(173, 173, 173));

		JPanel north = new JPanel(new BorderLayout(10, 0));
		north.add(new JLabel("Max number of assignments per table:",
				SwingConstants.RIGHT), BorderLayout.CENTER);
		north.add(sizes, BorderLayout.EAST);
		north.add(sep, BorderLayout.SOUTH);

		JButton add = new JButton("Add a Restriction Set");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addRestrictions(null);
			}
		});
		add.setFocusable(false);

		JButton save = new JButton("Save");
		save.addActionListener(new SaveActionListener());
		save.setActionCommand("restrictions");
		save.setFocusable(false);

		JPanel south = new JPanel(new GridLayout(1, 0));
		south.add(add);
		south.add(save);

		add(north, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
		add(south, BorderLayout.SOUTH);

		setBorder(BorderFactory.createEtchedBorder());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e) {
		Config.changeMaxTableAssignmentCount((Integer) e.getItem());
	}

	/**
	 * Adds the restrictions.
	 * 
	 * @param people
	 *            the people
	 */
	private void addRestrictions(List<Person> people) {
		final JPanel panel = new JPanel(new GridBagLayout());
		panel.setPreferredSize(new Dimension(393, 237));

		GridBagConstraints panelGbc = new GridBagConstraints();
		panelGbc.anchor = GridBagConstraints.CENTER;
		panelGbc.fill = GridBagConstraints.BOTH;
		panelGbc.weightx = 1;
		panelGbc.weighty = 1;

		final SelectStudentsPanel restrict = new SelectStudentsPanel(people);
		panel.add(restrict, panelGbc);

		panel.setBorder(BorderFactory.createMatteBorder(restrict.getId() % 2,
				0, 0, 0, Color.BLACK));

		JButton remove = new JButton("Remove this set");
		remove.setActionCommand("remove");
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restrict.removeFromDatabase();
				view.remove(panel);

				validate();
				repaint();
			}
		});
		remove.setFocusable(false);

		panelGbc.gridy = 1;
		panel.add(remove, panelGbc);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridy = restrict.getId() % 2;

		view.add(panel, gbc);

		validate();
	}

}
