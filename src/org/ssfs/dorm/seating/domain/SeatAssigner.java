/*
 * File Name	:		SeatAssigner.java
 * 
 * Last Updated	:		Dec 19, 2012 8:01:44 AM
 * 
 */

package org.ssfs.dorm.seating.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("javadoc")
public class SeatAssigner {

	private List<Person> people;

	private List<Table> tables;

	public SeatAssigner() {
		this(Table.getAllTables(), Person.getAllPeople());
	}

	@SuppressWarnings("unchecked")
	public SeatAssigner(List<? extends DinnerObject> tables,
			List<? extends DinnerObject> people) {
		Collections.shuffle(people);
		this.tables = (List<Table>) tables;
		this.people = (List<Person>) people;
	}

	public String toString() {
		String ret = "";
		String nl = System.getProperty("line.separator");
		for (Table t : tables) {
			ret += String.format("%s:%s%s%s", t.toString(), nl,
					t.makeAssignmentString(), nl);
		}
		ret += "=====================";
		return ret;
	}

	public List<String> getNAssignments(int n) {
		List<String> ret = new ArrayList<String>();

		String nl = System.getProperty("line.separator");

		for (int i = 0; i < n; i++) {
			if (assign()) {
				ret.add(nl + "Week " + (i + 1) + " : " + nl + nl + toString());
				for (Table t : tables) {
					t.clearTable();
				}
			} else {
				i--;
			}
		}

		return ret;
	}

	public boolean assign() {
		Collections.shuffle(people);
		return assign(0);
	}

	/**
	 * This is private so the backtracking always starts at the beginning. If I
	 * made it public, anyone could run the backtracker, and could potentially
	 * start it with bad data (i.e., <code>index < 0</code> or
	 * <code>index >= people.size()</code>).
	 * <p>
	 * Source: <a href=
	 * "https://github.com/kapild/Permutations/blob/master/src/EightQueen.java">
	 * Kapild's Eight Queens on github</a>
	 * </p>
	 */
	private boolean assign(int index) {
		// if the tables are all full,
		if (Table.validate()) {
			// we are done
			return true;
		}
		// if all the people have been assigned,
		if (index >= people.size()) {
			// we are done
			return true;
		}
		// get the next person
		Person p = people.get(index);
		// check each table
		for (Table t : tables) {
			// if this table is a perfect match,
			if (t.canSitHere(p) == ValidatorConstants.SEATED) {
				// assign them,
				t.seatPerson(p);
				// and recurse, continuing down this branch of the tree
				if (assign(index + 1)) {
					return true;
				}
				// if this branch ends up with no perfect options later on,
				// revert the assignment,
				t.unseatPerson(p);
			}
			// try the next table by continuing the loop.
		}
		// will only get here if it couldn't assign the person to any table.
		if (Table.cannotBeSeated(p)) {
			// if this is the case, find the "least bad" table to assign the
			// person to
			Table t = Table.leastBadTable(p);
			if (t == null) {
				return false;
			}
			// and assign them to the table.
			t.seatPerson(p);
			// recurse
			if (assign(index + 1)) {
				return true;
			}
			// revert if no configurations were found
			t.unseatPerson(p);
		}
		// this is not a valid branch, so return
		return false;
	}

	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("SeatAssigner.java");
			System.err.println();
			System.err.println("Usage:");
			System.err.println("\tjava SeatAssginer num_weeks person_file.csv"
					+ " table_file.csv");
			System.err.println();
			System.err.println("num_weeks: an integer for how many weeks to "
					+ "generate");
			System.err.println("File names are not important, but they"
					+ " need to contain the correct information.");
			return;
		}

		Config.importPeople(new File(args[1]));
		Config.importTables(new File(args[2]));

		SeatAssigner a = new SeatAssigner();

		for (String s : a.getNAssignments(Integer.parseInt(args[0]))) {
			System.out.println(s);
		}

		File f = new File("hist.csv");
		try {
			PrintWriter w = new PrintWriter(f);
			w.println(Config.generateHistoryReport());
			w.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// for (int i = 0; i < Integer.parseInt(args[0]); i++) {
		// SeatAssigner assigner = new SeatAssigner();
		// if (assigner.assign()) {
		// System.out.println();
		// System.out.println("Week " + (i + 1) + " : ");
		// System.out.println();
		// System.out.println(assigner.toString());
		// }
		// for (Table t : Table.getAllTables()) {
		// t.clearTable();
		// }
		// }
	}
}
