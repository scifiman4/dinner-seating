
package org.ssfs.dorm.seating.domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

@SuppressWarnings("javadoc")
public class RestrictionDatabase extends TreeMap<Person, Set<Person>> implements
		Serializable {

	private static RestrictionDatabase instance;

	private static final long serialVersionUID = 3745539601225598320L;

	private RestrictionDatabase() {
		super();
	}

	public void clear() {
		for (Person p : keySet()) {
			remove(p);
		}
	}

	public Set<List<Person>> getGroups() {
		Comparator<List<Person>> comp = new Comparator<List<Person>>() {
			public int compare(List<Person> orig1, List<Person> orig2) {
				Collections.sort(orig1);
				Collections.sort(orig2);

				// make copies of the original lists
				List<Person> mod1 = new ArrayList<Person>(orig1);
				List<Person> mod2 = new ArrayList<Person>(orig2);
				List<Person> mod3 = new ArrayList<Person>(orig1);
				List<Person> mod4 = new ArrayList<Person>(orig2);

				mod1.retainAll(orig2);
				mod2.retainAll(orig1);
				if (mod1.equals(mod2)) {
					return 0;
				}

				mod3.removeAll(orig2);
				mod4.removeAll(orig1);
				if (mod3.size() == mod4.size()) {
					int compare = 0;
					for (Person p1 : mod3) {
						for (Person p2 : mod4) {
							// no need to check for equality; if they are the
							// same, compareTo will return 0.
							compare += p1.compareTo(p2);
						}
					}
					return compare;
				}
				return mod1.size() - mod2.size();
			}
		};

		Set<List<Person>> toReturn = new TreeSet<List<Person>>(comp);
		for (Person key : keySet()) {
			List<Person> toAdd = new ArrayList<Person>(get(key));
			toAdd.add(key);

			toReturn.add(toAdd);
		}
		return toReturn;
	}

	public Set<Person> remove(Object key) {
		if (key != null && containsKey(key)) {
			return removeAllOf((Person) key);
		}
		return null;
	}

	public Set<Person> removeAllOf(Person... people) {
		if (people == null || people.length == 0) {
			return null;
		}
		Set<Person> removed = new TreeSet<Person>();
		List<Person> toRemove = Arrays.asList(people);
		for (Person key : toRemove) {
			if (containsKey(key)) {
				Set<Person> set = new TreeSet<Person>(get(key));
				for (Person p : set) {
					if (!toRemove.contains(p)) {
						continue;
					}
					if (!p.equals(key)) {
						try {
							get(key).remove(p);
							get(p).remove(key);
							removed.add(key);
							removed.add(p);
						} catch (NullPointerException e) {
						}
						if (set.size() == 1) {
							super.remove(key);
						}
					}
				}
			}
		}
		for (Person key : new TreeSet<Person>(keySet())) {
			if (get(key).size() == 0) {
				super.remove(key);
				removed.add(key);
			}
		}
		return removed;
	}

	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		if (in != null) {
			while (in.available() > 0) {
				Person key = (Person) in.readObject();
				Set<Person> value = (Set<Person>) in.readObject();
				put(key, value);
			}
		}
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		if (out != null) {
			for (Map.Entry<Person, Set<Person>> entry : entrySet()) {
				out.writeObject(entry.getKey());
				out.writeObject(entry.getValue());
			}
		}
	}

	public static RestrictionDatabase getInstance() {
		if (instance == null) {
			instance = new RestrictionDatabase();
		}
		return instance;
	}

}
