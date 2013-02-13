/*
 * File Name	:		Person.java
 * 
 * Last Updated	:		Dec 19, 2012 8:01:44 AM
 * 
 */

package org.ssfs.dorm.seating.domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("javadoc")
public class Person implements Comparable<Person>, DinnerObject, Serializable {

	private int id;

	private String name;

	private String nationality;

	private Table sittingAt;

	private int timesAssigned;

	private Map<Table, List<Integer>> pastCounts;

	private static int nextId;

	/** maps people's names to their object */
	private static Hashtable<String, Person> peopleHash;

	private static final long serialVersionUID = 6604347254378722998L;

	public Person(String name, String nationality) {
		this.name = name;
		this.nationality = nationality;
		timesAssigned = 0;
		id = nextId++;
		pastCounts = new TreeMap<Table, List<Integer>>();
		if (peopleHash == null) {
			peopleHash = new Hashtable<String, Person>();
		}
		peopleHash.put(name, this);
	}

	private Person(Person o) {
		this(o.name, o.nationality);
	}

	private Person() {
		name = "";
		nationality = "";
	}

	public int compareTo(Person o) {
		return id - o.id;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((nationality == null) ? 0 : nationality.hashCode());
		return result;
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
		Person other = (Person) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (nationality == null) {
			if (other.nationality != null) {
				return false;
			}
		} else if (!nationality.equals(other.nationality)) {
			return false;
		}
		return true;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNationality() {
		return nationality;
	}

	public String getSecondField() {
		return nationality;
	}

	public String toString() {
		return name;
	}

	void assignTo(Table t) {
		sittingAt = t;
		List<Integer> times;
		if (!pastCounts.containsKey(t)) {
			pastCounts.put(t, new ArrayList<Integer>());
		}
		times = pastCounts.get(t);
		times.add(++timesAssigned);
	}

	int getLastSatHere(Table t) {
		if (!pastCounts.containsKey(t)) {
			return 0;
		}
		return pastCounts.get(t).get(pastCounts.get(t).size() - 1);
	}

	int getTimesSatHere(Table t) {
		return !pastCounts.containsKey(t) ? 0 : pastCounts.get(t).size();
	}

	boolean satHereLastTime(Table t) {
		int lastDate = getLastSatHere(t);
		return lastDate > 0 && lastDate == timesAssigned - 1;
	}

	void setName(String name) {
		this.name = name;
	}

	void setNationality(String nationality) {
		this.nationality = nationality;
	}

	void unseat() {
		if (sittingAt == null) {
			return;
		}
		if (pastCounts.containsKey(sittingAt)) {
			List<Integer> pastSeatings = pastCounts.get(sittingAt);
			pastSeatings.remove(pastSeatings.size() - 1);
			if (pastCounts.size() == 0) {
				pastCounts.remove(sittingAt);
			}
		}
		timesAssigned--;
		sittingAt = null;
	}

	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		id = in.readInt();
		name = (String) in.readObject();
		nationality = (String) in.readObject();
		pastCounts = (Map<Table, List<Integer>>) in.readObject();

		if (id > nextId) {
			nextId = id + 1;
		}
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(id);

		out.writeObject(name);
		out.writeObject(nationality);
		out.writeObject(pastCounts);
	}

	public static List<Person> getAllPeople() {
		if (peopleHash == null) {
			peopleHash = new Hashtable<String, Person>();
		}
		return new ArrayList<Person>(peopleHash.values());
	}

	public static Person getPerson(String name) {
		if (peopleHash.containsKey(name)) {
			return new Person(peopleHash.get(name));
		}
		return null;
	}

	static void load(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		int count = in.readInt();
		for (int i = 0; i < count; i++) {
			Person p = new Person();
			p.readObject(in);
		}
	}

	static void save(ObjectOutputStream out) throws IOException {
		out.writeInt(peopleHash.size());
		for (Person p : peopleHash.values()) {
			p.writeObject(out);
		}
	}

}
