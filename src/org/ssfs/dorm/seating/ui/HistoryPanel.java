/*
 * File Name:
 * HistoryPanel.java
 * 
 * Last Updated:
 * Jul 6, 2012 10:11:55 PM
 * 
 */

package org.ssfs.dorm.seating.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.ssfs.dorm.seating.domain.Person;
import org.ssfs.dorm.seating.domain.Table;

/**
 * The Class HistoryPanel.
 * 
 * @author Matthew Denaburg '10
 */
public class HistoryPanel extends DinnerPanel {

	/** The table. */
	private final JTable table;

	/** The Constant name. */
	public static final String name = "History";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new history panel.
	 */
	public HistoryPanel() {
		super();

		final TableModel model = new DefaultTableModel(makeRows(),
				makeColumnTitles()) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(model);

		table.getColumnModel().setColumnMargin(5);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setRowHeight(30);
		table.setRowSorter(new TableRowSorter<TableModel>(model));

		JButton clear = new JButton("Clear History");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(
						SwingUtilities.getRoot(table),
						"Are you sure you want to clear the history?",
						"Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					Config.clearHistory();
					refresh();
				}
			}
		});
		clear.setFocusable(false);

		JScrollPane scrollPane = new JScrollPane(table);

		JPanel panel = new JPanel(new BorderLayout(0, 5));
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(new JLabel("Past Table Assignments", SwingConstants.CENTER),
				BorderLayout.NORTH);
		panel.add(clear, BorderLayout.SOUTH);

		add(panel, BorderLayout.CENTER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ssfs.dorm.ui.DinnerPanel#getMyName()
	 */
	public String getMyName() {
		return name;
	}

	/**
	 * Refresh.
	 */
	public void refresh() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setDataVector(makeRows(), makeColumnTitles());
		model.fireTableDataChanged();
	}

	/**
	 * Make column titles.
	 * 
	 * @return the vector
	 */
	private Vector<Object> makeColumnTitles() {
		Vector<Object> columnNames = new Vector<Object>();
		columnNames.add("Person");
		for (Table t : Config.getTableDatabase()) {
			columnNames.add(t.getName());
		}
		return columnNames;
	}

	/**
	 * Make row.
	 * 
	 * @return the vector
	 */
	private Vector<Vector<Object>> makeRows() {
		Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
		for (Person p : Config.getPeopleDatabase()) {
			Vector<Object> row = new Vector<Object>();
			row.add(p.getName());
			for (Table t : Config.getTableDatabase()) {
				row.add(p.getCount(t));
			}
			rows.add(row);
		}
		return rows;
	}

}
