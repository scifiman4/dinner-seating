
package tests.org.ssfs.dorm.seating.domain;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.ssfs.dorm.seating.domain.Person;
import org.ssfs.dorm.seating.domain.Table;

@SuppressWarnings("javadoc")
public class TestTable extends TestCase {

	private List<Table> tables;

	private List<Person> people;

	protected void setUp() throws Exception {
		super.setUp();

		tables = new ArrayList<Table>();

		tables.add(new Table("Matthew", 4));
		tables.add(new Table("Jason", 4));
		tables.add(new Table("David", 4));
		tables.add(new Table("Olivia", 4));

		people = new ArrayList<Person>();
		people.add(new Person("Bob 1", "United States"));
		people.add(new Person("Bob 2", "United States"));
		people.add(new Person("Bob 3", "United States"));
		people.add(new Person("Bob 4", "United States"));
		people.add(new Person("Joe 1", "China"));
		people.add(new Person("Joe 2", "China"));
		people.add(new Person("Joe 3", "China"));
		people.add(new Person("Joe 4", "China"));
		people.add(new Person("Mary 1", "Russia"));
		people.add(new Person("Mary 2", "Russia"));
		people.add(new Person("Mary 3", "Russia"));
		people.add(new Person("Mary 4", "Russia"));
		people.add(new Person("Jan 1", "Austria"));
		people.add(new Person("Jan 2", "Austria"));
		people.add(new Person("Jan 3", "Austria"));
		people.add(new Person("Jan 4", "Austria"));
	}

	public void testCanSitHere() {
		fail("Not yet implemented"); // TODO
	}

	public void testClearTable() {
		Table t = new Table("Matthew", 1);
		Person p = new Person("Alan", "Japan");

		t.seatPerson(p);
	}

	public void testCompareTo() {
		Table t = new Table("Matthew", 0);
		Table s = new Table("Jason", 0);

		assertTrue(t.compareTo(t) == 0);
		assertTrue(t.compareTo(s) < 0);
		assertTrue(s.compareTo(t) > 0);
	}

	public void testEqualsObject() {
		Table t = new Table("Matthew", 0);
		Table s = new Table("Jason", 0);

		assertEquals(t, t);
		assertFalse(!t.equals(s));
	}

	public void testGetMembers() {
		fail("Not yet implemented"); // TODO
	}

	public void testGetName() {
		Table t = new Table("Matthew", 0);
		String curr = t.getName();

		assertEquals("Matthew", curr);
	}

	public void testGetSecondField() {
		fail("Not yet implemented"); // TODO
	}

	public void testGetTableSize() {
		Table t = new Table("", 4);
		int curr = t.getTableSize();
		assertEquals(4, curr);
	}

	public void testSetName() {
		Table t = new Table("Matthew", 0);
		String curr = "Matthew";

		t.setName("Alan");
		assertEquals("Alan", t.getName());

		t.setName(curr);
		assertEquals(curr, t.getName());
	}

	public void testSetTableSize() {
		fail("Not yet implemented"); // TODO
	}

	public void testSeatPerson() {
		fail("Not yet implemented"); // TODO
	}

	public void testUnseatPerson() {
		fail("Not yet implemented"); // TODO
	}

	public void testGetAllTables() {
		fail("Not yet implemented"); // TODO
	}

	public void testCannotBeSeated() {
		fail("Not yet implemented"); // TODO
	}

	public void testLeastBadTable() {
		fail("Not yet implemented"); // TODO
	}

	public void testValidate() {
		fail("Not yet implemented"); // TODO
	}

}
