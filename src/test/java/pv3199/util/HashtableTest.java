package pv3199.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HashtableTest {
	private final static Class<? extends Throwable> IAE = IllegalArgumentException.class;
	private final static int[] values = { -132, 65, 83, 932, 78, 31, 97, 211 };

	private Integer[] expectedTable;
	private Hashtable<Integer> table;

	@BeforeEach
	public void resetTable() {
		expectedTable = new Integer[]{ 211, 65, 97, 83, -132, 932, 78, 31 };
		table = new Hashtable<>(4);
	}

	@Test
	public void constructorTest() {
		assertEquals(0, new Hashtable().size());
		assertThrows(IAE, () -> new Hashtable(0));
		assertThrows(IAE, () -> new Hashtable<>(Object::hashCode, 0));
	}
	
	@Test
	public void addTest() {
		for (int i : values) {
			table.add(i);
		}

		assertArrayEquals(expectedTable, table.data());
		assertEquals(values.length, table.size());
	}

	@Test
	public void containsTest() {
		addTest();

		for (int i : values) {
			assertTrue(table.contains(i));
		}

		assertFalse(table.contains(0));
		assertFalse(table.contains(Integer.MAX_VALUE));
	}

	@Test
	public void removeTest() {
		addTest();

		int[] removeOrder = { 97, 932, 83, 78, 31, 65, -132, 211 };

		for (int i = 0; i < removeOrder.length; i++) {
			int rem = removeOrder[i];

			assertTrue(table.remove(rem));
			assertFalse(table.remove(rem));

			int index = Arrays.indexOf(expectedTable, rem)[0];
			expectedTable[index] = null;

			assertArrayEquals(expectedTable, table.data());
			assertEquals(expectedTable.length - i - 1, table.size());
		}
	}
}
