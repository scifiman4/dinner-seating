/*
 * File Name	:		Restriction.java
 * 
 * Last Updated	:		Dec 28, 2012 11:09:25 AM
 * 
 */

package org.ssfs.dorm.seating.domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * The {@code Restriction} class is similar to {@link Map.Entry} in that it is a
 * {@code key-value} pair. The {@code key} is a {@link Person} object, and the
 * {@code value} is a {@link Set} of {@link Person} objects.
 * <p>
 * Each {@code Person} in {@code value} is someone that cannot sit with the
 * {@code Person} in {@code key}.
 * </p>
 * <p>
 * 
 * </p>
 */
@SuppressWarnings("javadoc")
class Restriction implements Comparable<Restriction>, Serializable {

	private int id;

	private Person keyPerson;

	private Set<Person> cannotSitWithUs;

	private static int nextId = 0;

	private static Set<Set<Person>> groups;

	private static Map<Person, Restriction> database;

	private static final long serialVersionUID = 3745539601225598320L;

	private Restriction(Person p, Set<Person> notWith) {
		id = nextId++;
		keyPerson = p;
		this.cannotSitWithUs = new TreeSet<Person>(notWith);
	}

	public int compareTo(Restriction o) {
		if (o == null) {
			throw new NullPointerException();
		}
		return id - o.id;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result
				+ ((keyPerson == null) ? 0 : keyPerson.hashCode());
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
		Restriction other = (Restriction) obj;
		if (id != other.id) {
			return false;
		}
		if (keyPerson == null) {
			if (other.keyPerson != null) {
				return false;
			}
		} else if (!keyPerson.equals(other.keyPerson)) {
			return false;
		}
		return true;
	}

	private boolean canSitWithUs(Set<Person> others) {
		return setIntersect(cannotSitWithUs, others).size() == 0;
	}

	/**
	 * Returns a set representing all of the unique groups of people that cannot
	 * sit together.
	 * <p>
	 * <u>Example</u>:<br>
	 * If the following people cannot sit with each other, this method would
	 * return
	 * </p>
	 * 
	 * <pre>
	 * { { James, Sam, Nate, Norman }, { James, Madison, Madeleine }, { James, Ben } }
	 * </pre>
	 * 
	 * @return a set of sets of people that cannot sit together.
	 */
	public static Set<Set<Person>> getGroups() {
		if (groups == null) {
			groups = new TreeSet<Set<Person>>();
		}
		return new TreeSet<Set<Person>>(groups);
	}

	public static boolean cannotSitWith(Person p, Set<Person> others) {
		if (database.containsKey(p)) {
			return database.get(p).canSitWithUs(others);
		}
		return true;
	}

	public static void makeAddRestrictions(Set<Person> people) {
		if (database == null) {
			database = new HashMap<Person, Restriction>();
		}
		if (groups == null) {
			groups = new TreeSet<Set<Person>>();
		}

		for (Person p : people) {
			Set<Person> notWithUs = new TreeSet<Person>(people);
			// they have to be able to sit with themselves
			notWithUs.remove(p);

			// if the person p already has restrictions, add to them
			if (database.containsKey(p)) {
				database.get(p).cannotSitWithUs.addAll(notWithUs);
			} else {
				database.put(p, new Restriction(p, notWithUs));
			}
		}

	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		if (in != null) {
			keyPerson = (Person) in.readObject();
			for (int i = 0; i < in.readInt(); i++) {
				cannotSitWithUs.add((Person) in.readObject());
			}
		}
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		if (out != null) {
			out.writeObject(keyPerson);
			out.writeInt(cannotSitWithUs.size());
			for (Person p : cannotSitWithUs) {
				out.writeObject(p);
			}
		}
	}

	private static Set<Person> setIntersect(Set<Person> setA, Set<Person> setB) {
		Set<Person> tmp = new TreeSet<Person>();
		for (Person x : setA) {
			if (setB.contains(x)) {
				tmp.add(x);
			}
		}
		return tmp;
	}

	/**
	 * <ul>
	 * <li>James ->
	 * <ul>
	 * <li>Sam</li>
	 * <li>Nate</li>
	 * <li>Norman</li>
	 * <li>Ben</li>
	 * </ul>
	 * </li>
	 * <li>Sam ->
	 * <ul>
	 * <li>James</li>
	 * <li>Nate</li>
	 * <li>Norman</li>
	 * <li>Madison</li>
	 * <li>Madeleine</li>
	 * </ul>
	 * </li>
	 * <li>Nate ->
	 * <ul>
	 * <li>James</li>
	 * <li>Sam</li>
	 * <li>Norman</li>
	 * </ul>
	 * </li>
	 * <li>Norman ->
	 * <ul>
	 * <li>James</li>
	 * <li>Sam</li>
	 * <li>Nate</li>
	 * </ul>
	 * </li>
	 * <li>Madison ->
	 * <ul>
	 * <li>Sam</li>
	 * <li>Madeleine</li>
	 * </ul>
	 * </li>
	 * <li>Madeleine ->
	 * <ul>
	 * <li>Sam</li>
	 * <li>Madison</li>
	 * </ul>
	 * </li>
	 * <li>Ben ->
	 * <ul>
	 * <li>James</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * becomes...
	 * 
	 * <pre>
	 * { { James, Sam, Nate, Norman }, { Sam, Madison, Madeleine }, { James, Ben } }
	 * </pre>
	 */
	@SuppressWarnings("unused")
	private static void makeGroups() {

	}

	private static Set<Person> setDiff(Set<Person> setA, Set<Person> setB) {
		Set<Person> tmp = new TreeSet<Person>(setA);
		tmp.removeAll(setB);
		return tmp;
	}

	@SuppressWarnings("unused")
	private static Set<Person> symDiff(Set<Person> setA, Set<Person> setB) {
		return setDiff(setUnion(setA, setB), setIntersect(setA, setB));
	}

	private static Set<Person> setUnion(Set<Person> setA, Set<Person> setB) {
		Set<Person> tmp = new TreeSet<Person>(setA);
		tmp.addAll(setB);
		return tmp;
	}

}
