
package org.ssfs.dorm.seating.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.ssfs.dorm.seating.domain.Config;
import org.ssfs.dorm.seating.domain.Person;

/**
 * The Class SelectStudentsPanel.
 */
class SelectStudentsPanel extends JPanel implements ActionListener,
		ListCellRenderer<Person>, ListSelectionListener {

	/** The count. */
	private int id;

	/** The list. */
	private JList<Person> list;

	/** The myPeople. */
	private Vector<Person> myPeople;

	/** The remove button. */
	private JButton remove;

	/** The data. */
	private static RestrictionPanel data;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The usedIds. */
	private static SortedSet<Integer> usedIds;

	/**
	 * Instantiates a new restriction panel.
	 * 
	 * @param people
	 *            the people
	 */
	public SelectStudentsPanel(List<Person> people) {
		super(new BorderLayout());
		if (data == null) {
			data = Config.getRestrictionDatabase();
		}
		if (usedIds == null) {
			usedIds = new TreeSet<Integer>();
		}

		myPeople = new Vector<Person>();
		if (people != null) {
			myPeople.addAll(people);
		}

		remove = new JButton("Remove selected");
		remove.setActionCommand("remove");
		remove.addActionListener(this);
		remove.setFocusable(false);
		remove.setEnabled(false);

		list = new JList<Person>(myPeople);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setFixedCellWidth(100);
		list.addListSelectionListener(this);
		list.setCellRenderer(this);

		JScrollPane scrollPane = new JScrollPane(list);

		JButton add = new JButton("Add people");
		add.setActionCommand("add");
		add.addActionListener(this);
		add.setFocusable(false);

		id = getNextId();

		JLabel label = new JLabel("Restriction Set " + (id + 1));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBorder(BorderFactory.createEmptyBorder(3, 0, 2, 0));

		JPanel south = new JPanel(new GridLayout(1, 0));
		south.add(add);
		south.add(remove);

		add(label, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(south, BorderLayout.SOUTH);

		setPreferredSize(new Dimension(290, 200));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("add")) {
			for (Person p : ListDialog.showDialog(this, null, "",
					"Select People", Config.getPeopleDatabase().getData()
							.toArray(), "", " ")) {
				if (!myPeople.contains(p)) {
					myPeople.add(p);
				}
			}
			list.setListData(myPeople);
			addPeopleToDatabase();
		} else if (e.getActionCommand().equals("remove")) {
			for (Object obj : list.getSelectedValues()) {
				myPeople.remove(obj);
				Config.removeRestriction(new Person[] { (Person) obj });
			}
		}
		validate();
		repaint();
	}

	/**
	 * Gets the count.
	 * 
	 * @return the count
	 */
	public int getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing
	 * .JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList<? extends Person> list,
			Person value, int index, boolean isSelected, boolean cellHasFocus) {
		JLabel label = new JLabel(value.toString());
		label.setBorder(BorderFactory.createEmptyBorder(1, 3, 0, 0));
		if (isSelected) {
			label.setBackground(list.getSelectionBackground());
			label.setForeground(list.getSelectionForeground());
		} else {
			label.setBackground(Color.WHITE);
			label.setForeground(list.getForeground());
		}
		label.setOpaque(true);
		return label;
	}

	/**
	 * Gets the myPeople.
	 * 
	 * @return the myPeople
	 */
	public List<Person> getPeople() {
		return new ArrayList<Person>(myPeople);
	}

	/**
	 * Removes this restriction set from the database.
	 */
	public void removeFromDatabase() {
		Config.removeRestriction(myPeople.toArray(new Person[myPeople.size()]));
		removeId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
	 * .ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		remove.setEnabled(list.getSelectedIndices().length > 0);
	}

	/**
	 * Adds the myPeople to database.
	 */
	private void addPeopleToDatabase() {
		if (myPeople.size() > 1) {
			Config.addRestrictions(myPeople.toArray(new Person[myPeople.size()]));
		}
	}

	/**
	 * Removes the id.
	 */
	private void removeId() {
		usedIds.remove(id);
	}

	/**
	 * Gets the next id.
	 * 
	 * @return the next id
	 */
	private static int getNextId() {
		int start;
		if (usedIds.size() == 0 || usedIds.last() != usedIds.size()) {
			start = 0;
		} else {
			start = usedIds.last();
		}
		while (usedIds.contains(start)) {
			start++;
		}
		usedIds.add(start);
		return start;
	}

}
