/*
 * File Name	:		Table.java
 * 
 * Last Updated	:		Dec 23, 2012 9:45:28 AM
 * 
 */

package org.ssfs.dorm.seating.domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings("javadoc")
public class Table implements Comparable<Table>, DinnerObject, Serializable {

	/** The table's id. */
	private int id;

	/** who the table host is */
	private String name;

	/** how many seats the table has. */
	private int tableSize;

	/** who is sitting at the table */
	private Set<Person> members;

	/** the next id */
	private static int nextId;

	private static final long serialVersionUID = 7802939084043227619L;

	/** all of the tables */
	private static List<Table> tables;

	public Table(String name, int size) {
		this.name = name;
		tableSize = size;
		id = nextId++;
		members = new TreeSet<Person>();
		if (tables == null) {
			tables = new ArrayList<Table>();
		}
		tables.add(this);
	}

	/**
	 * Instantiates a new table. This is used by
	 * {@link #load(ObjectInputStream)}
	 */
	private Table() {
		name = "";
		tableSize = 0;
	}

	public int canSitHere(Person p) {
		// table full
		if (tableSize == members.size()) {
			return ValidatorConstants.FULL;
		}
		// cannot sit with people already here
		RestrictionDatabase rdb = RestrictionDatabase.getInstance();
		if (!Collections.disjoint(rdb.get(p), members)) {
			return ValidatorConstants.DISJOINT;
		}
		// can't sit more than X times
		if (p.getTimesSatHere(this) > Config.MAX_TIMES_AT_TABLE) {
			return ValidatorConstants.TOO_MANY_TIMES;
		}
		// sat here last time
		if (p.satHereLastTime(this)) {
			return ValidatorConstants.LAST_TIME;
		}
		// or if too many people of the same nationality are already here
		List<String> countryCounts = new ArrayList<String>();
		for (Person i : members) {
			countryCounts.add(i.getNationality());
		}
		int freq = Collections.frequency(countryCounts, p.getNationality());
		if (freq > Config.MAX_PEOPLE_FROM_SAME_COUNTRY) {
			return ValidatorConstants.COUNTRIES;
		}
		return ValidatorConstants.SEATED;
	}

	public void clearTable() {
		members.clear();
	}

	public int compareTo(Table o) {
		return id - o.id;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Table other = (Table) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	public int getId() {
		return id;
	}

	public Set<Person> getMembers() {
		return members;
	}

	public String getName() {
		return name;
	}

	public String getSecondField() {
		return "" + tableSize;
	}

	public int getTableSize() {
		return tableSize;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTableSize(int tableSize) {
		this.tableSize = tableSize;
	}

	public String makeAssignmentString() {
		String members = "";
		for (Person p : this.members) {
			members += String.format("  %s%s", p.toString(),
					System.getProperty("line.separator"));
		}
		return members;
	}

	public String toString() {
		return name;
	}

	boolean seatPerson(Person p) {
		if (members.size() >= tableSize) { // if the table is full
			return false;
		}
		p.assignTo(this);
		return members.add(p);
	}

	boolean unseatPerson(Person p) {
		p.unseat();
		return members.remove(p);
	}

	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		id = in.readInt();
		tableSize = in.readInt();
		name = (String) in.readObject();
		members = (Set<Person>) in.readObject();

		if (id > nextId) {
			nextId = id;
		}
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(id);
		out.writeInt(tableSize);
		out.writeObject(name);
		out.writeObject(members);
	}

	public static List<Table> getAllTables() {
		if (tables == null) {
			tables = new ArrayList<Table>();
		}
		return new ArrayList<Table>(tables);
	}

	static boolean cannotBeSeated(Person p) {
		for (Table t : tables) {
			if (t.canSitHere(p) == ValidatorConstants.SEATED) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Find the least bad table to assign the person to.
	 * 
	 * @param p
	 *            the person to assign
	 * @return the table
	 */
	static Table leastBadTable(Person p) {
		int leastBadValue = Integer.MAX_VALUE;
		Table leastBadTable = null;
		for (Table t : tables) {
			int value = t.canSitHere(p);
			if (value == ValidatorConstants.FULL) {
				continue;
			}
			if (leastBadValue > value) {
				leastBadTable = t;
				leastBadValue = value;
			}
		}
		return leastBadTable;
	}

	static void load(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		int count = in.readInt();
		for (int i = 0; i < count; i++) {
			Table t = new Table();
			t.readObject(in);
		}
	}

	static void save(ObjectOutputStream out) throws IOException {
		out.writeInt(tables.size());
		for (Table t : tables) {
			t.writeObject(out);
		}
	}

	static boolean validate() {
		for (Table t : tables) {
			if (t.getTableSize() != t.getMembers().size()) {
				return false;
			}
		}
		return true;
	}

}
