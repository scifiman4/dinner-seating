/*
 * File Name	:		Config.java
 * 
 * Last Updated	:		Dec 19, 2012 8:01:23 AM
 * 
 */

package org.ssfs.dorm.seating.domain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings("javadoc")
public class Config {

	/** How many weeks table assignments last for. */
	// TODO customizable assignment length
	public static int ASSIGNMENT_LENGTH = 2;

	/** How many people from the same country can sit together. */
	public static int MAX_PEOPLE_FROM_SAME_COUNTRY = 3;

	/** How many times any given person can sit at a certain table. */
	public static int MAX_TIMES_AT_TABLE = 4;

	public static String generateHistoryReport() {
		List<Person> people = Person.getAllPeople();
		List<Table> tables = Table.getAllTables();

		Collections.sort(people);
		Collections.sort(tables);

		StringBuilder ret = new StringBuilder();
		String nl = System.getProperty("line.separator");

		ret.append("name,");
		for (int i = 0; i < tables.size() - 1; i++) {
			ret.append(tables.get(i).getName() + ",");
		}
		ret.append(tables.get(tables.size() - 1));
		ret.append(nl);

		for (Person p : people) {
			ret.append(p.getName() + ",");
			Table t;
			for (int i = 0; i < tables.size() - 1; i++) {
				t = tables.get(i);
				ret.append(p.getTimesSatHere(t) + ",");
			}
			t = tables.get(tables.size() - 1);
			ret.append(p.getTimesSatHere(t));
			ret.append(nl);
		}

		return ret.toString();
	}

	public static void export(boolean tables, File f) {
		try {
			PrintWriter w = new PrintWriter(f);

			for (DinnerObject d : Config.getType(tables)) {
				w.println(d.getName() + "," + d.getSecondField());
			}

			w.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Person getPersonByName(String id) {
		for (Person d : Person.getAllPeople()) {
			if (d.getName().equals(id)) {
				return d;
			}
		}
		return null;
	}

	public static Table getTableByName(String id) {
		for (Table d : Table.getAllTables()) {
			if (d.getName().equals(id)) {
				return d;
			}
		}
		return null;
	}

	public static List<? extends DinnerObject> getType(boolean wantsTables) {
		return wantsTables ? Table.getAllTables() : Person.getAllPeople();
	}

	public static List<Table> importTables(File f) {
		List<Table> ret = new ArrayList<Table>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(f));
			String line;
			while ((line = in.readLine()) != null) {
				String[] split = line.split(",");
				ret.add(new Table(split[0], Integer.parseInt(split[1])));
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return ret;
	}

	public static List<Person> importPeople(File f) {
		List<Person> ret = new ArrayList<Person>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(f));

			List<String[]> lines = new ArrayList<String[]>();

			String line;
			while ((line = in.readLine()) != null) {
				String[] split = line.split(",");
				if (split.length > 2) {
					lines.add(split);
				}
				ret.add(new Person(split[0], split[1]));
			}
			in.close();

			for (String[] lineArray : lines) {
				addRestriction(lineArray);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return ret;
	}

	public static boolean loadConfiguration() {
		try {
			File parentFile = makeParent();

			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					new File(parentFile, "table.cfg")));
			Table.load(in);
			in.close();

			in = new ObjectInputStream(new FileInputStream(new File(parentFile,
					"people")));
			Person.load(in);
			in.close();

			in = new ObjectInputStream(new FileInputStream(new File(parentFile,
					"constants.cfg")));
			MAX_TIMES_AT_TABLE = in.readInt();
			MAX_PEOPLE_FROM_SAME_COUNTRY = in.readInt();
			ASSIGNMENT_LENGTH = in.readInt();
			in.close();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static boolean saveConfiguration() {
		try {
			File parentFile = makeParent();

			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(new File(parentFile, "constants.cfg")));
			out.writeInt(MAX_TIMES_AT_TABLE);
			out.writeInt(MAX_PEOPLE_FROM_SAME_COUNTRY);
			out.writeInt(ASSIGNMENT_LENGTH);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return savePeople() && saveTables();
	}

	public static boolean savePeople() {
		File parent = makeParent();
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(new File(parent, "people.cfg")));
			Person.save(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean saveTables() {
		File parent = makeParent();
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(new File(parent, "table.cfg")));
			Table.save(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static void addRestriction(String[] restrict) {
		RestrictionDatabase rdb = RestrictionDatabase.getInstance();

		List<Person> restrictList = new ArrayList<Person>();
		for (String r : restrict) {
			restrictList.add(getPersonByName(r));
		}

		for (int i = 0; i < restrictList.size(); i++) {
			Person key = restrictList.get(i);
			Set<Person> value = new TreeSet<Person>();
			for (int j = 0; j < restrictList.size(); j++) {
				if (i == j) {
					continue;
				}
				value.add(restrictList.get(j));
			}
			rdb.put(key, value);
		}
	}

	private static File makeParent() {
		File parent = new File(System.getProperty("user.home")
				+ System.getProperty("file.separator") + ".seating-config");

		if (!parent.exists()) {
			parent = parent.getAbsoluteFile();
			parent.mkdir();
			if (System.getProperty("os.name").contains("indows")) {
				try {
					Process p = Runtime.getRuntime().exec(
							"attrib +H " + parent.getAbsolutePath());
					p.waitFor();
				} catch (InterruptedException | IOException e) {
				}
			}
		}

		return parent;
	}

}
