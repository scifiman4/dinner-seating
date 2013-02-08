
package org.ssfs.dorm.seating.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.ssfs.dorm.seating.domain.Config;
import org.ssfs.dorm.seating.domain.DinnerObject;
import org.ssfs.dorm.seating.domain.Table;
import org.ssfs.dorm.seating.ui.actions.AddAction;
import org.ssfs.dorm.seating.ui.actions.ExportAction;
import org.ssfs.dorm.seating.ui.actions.LoadAction;
import org.ssfs.dorm.seating.ui.actions.RemoveAction;
import org.ssfs.dorm.seating.ui.actions.RemoveEnabler;
import org.ssfs.dorm.seating.ui.actions.SaveActionListener;

/**
 * This is the panel that is used to configure the system.
 */
public class ConfigurePanel extends DinnerPanel implements ChangeListener {

	/** The config tabs. */
	private JTabbedPane configTabs;

	/** The Constant name. */
	public static final String name = "Configuration";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant tabTitles. */
	private static final String[] tabTitles = new String[] { "Tables",
			"Students", "Restrictions" };

	/**
	 * Instantiates a new configure panel.
	 */
	public ConfigurePanel() {
		super();

		configTabs = new JTabbedPane();
		configTabs.addChangeListener(this);
		configTabs.addTab(tabTitles[0], makeJTablePanel(true));
		configTabs.addTab(tabTitles[1], makeJTablePanel(false));
		// TODO unhide restriction panel
		// configTabs.addTab(tabTitles[2], new RestrictionPanel());
		configTabs.setFocusable(false);

		add(configTabs, BorderLayout.CENTER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ssfs.dorm.ui.DinnerPanel#getMyName()
	 */
	public String getMyName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent
	 * )
	 */
	public void stateChanged(ChangeEvent e) {
		changeLocationLabel("Configuration > "
				+ configTabs.getTitleAt(configTabs.getSelectedIndex()));
	}

	/**
	 * Makes a JTable for the appropriate type.
	 * 
	 * @param isTable
	 *            the is table
	 * @return the j panel
	 */
	private static JPanel makeJTablePanel(final boolean isTable) {
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		for (DinnerObject d : Config.getType(isTable)) {
			Vector<Object> row = new Vector<Object>();
			row.add(d.getName());
			row.add(d.getSecondField());
			data.add(row);
		}

		Vector<String> names = new Vector<String>(Arrays.asList(new String[] {
				"Name", (isTable ? "Size" : "Nationality") }));

		final DefaultTableModel model = new DefaultTableModel(data, names) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return isTable;
			}
		};

		JTable table = new JTable(model) {
			private static final long serialVersionUID = 1L;

			public void editingStopped(ChangeEvent e) {
				TableCellEditor editor = getCellEditor();
				if (editor != null) {
					Object newValue = editor.getCellEditorValue();

					Object oldValue = model.getValueAt(editingRow, 0);

					Table t = Config.getTableByName(oldValue.toString());

					if (editingColumn == 0) {
						t.setName(newValue.toString());
					} else {
						t.setTableSize(Integer.parseInt(newValue.toString()));
					}

					model.setValueAt(newValue, editingRow, editingColumn);
					setValueAt(newValue, editingRow, editingColumn);
					removeEditor();
				}
			}
		};

		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);

		Font f = table.getTableHeader().getFont().deriveFont(13f);
		table.getTableHeader().setFont(f);

		table.setColumnSelectionAllowed(false);
		table.setFocusTraversalPolicyProvider(false);
		table.setRowHeight(30);
		table.setRowSorter(new TableRowSorter<TableModel>(model));

		table.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			public Component getTableCellRendererComponent(JTable thisTable,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				DinnerObject val = (DinnerObject) value;

				return super.getTableCellRendererComponent(thisTable,
						column == 0 ? val.getName() : val.getSecondField(),
						isSelected, hasFocus, row, column);
			}
		});

		JButton add = new JButton("Add");
		JButton remove = new JButton("Remove");
		JButton load = new JButton("Importer");
		JButton export = new JButton("Exporter");
		JButton save = new JButton("Save");

		table.getSelectionModel().addListSelectionListener(
				new RemoveEnabler(table, remove));

		add.addActionListener(new AddAction(model, isTable));
		add.setFocusable(false);

		remove.addActionListener(new RemoveAction(table, model, isTable));
		remove.setFocusable(false);
		remove.setEnabled(false);

		load.addActionListener(new LoadAction(model, isTable));
		load.setActionCommand(isTable ? "tables" : "people");
		load.setFocusable(false);

		export.addActionListener(new ExportAction(isTable));
		export.setActionCommand(isTable ? "tables" : "people");
		export.setFocusable(false);

		save.addActionListener(new SaveActionListener());
		save.setActionCommand(isTable ? "tables" : "people");
		save.setFocusable(false);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 0));
		buttonPanel.add(add);
		buttonPanel.add(remove);
		buttonPanel.add(load);
		buttonPanel.add(export);
		buttonPanel.add(save);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setFocusable(false);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		return panel;
	}

}
