package pv3199.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BinaryArrayTest {
	private BinaryArray<Integer> binArr;
	private BinaryArray<Integer> binArr2;
	private int[] values = { 10, 5, 18, 2, 7, 11, 19, 1, 3, 6, 8, 16, 20, 0, 4, 9, 13, 17, 12, 14, 15 };

	@BeforeEach
	public void resetArrays() {
		binArr = new BinaryArray<>();
		binArr2 = new BinaryArray<>(15);
	}

    @Test
    public void constructorTest() {
		assertEquals(0, binArr.size());
		assertEquals(0, binArr2.size());

		assertThrows(IllegalArgumentException.class, () -> {
			new BinaryArray<Integer>(-1);
		});
	}
	
	@Test
	public void addTest() {
		for (int v : values) {
			binArr.add(v);
			binArr2.add(v);
		}

		assertEquals(values.length, binArr.size());
		assertEquals(values.length, binArr.size());

		System.out.println(binArr);
		System.out.println();
	}

	@Test
	public void heightTest() {
		addTest();

		assertEquals(7, binArr.height());
		assertEquals(7, binArr2.height());
	}

	@Test
	public void containsTest() {
		addTest();

		for (int v : values) {
			assertTrue(binArr.contains(v));
			assertTrue(binArr2.contains(v));
		}

		assertFalse(binArr.contains(-1341));
		assertFalse(binArr.contains(-4132));
	}

	@Test
	public void removeTest() {
		addTest();

		int[] toRemove = { 5, 8, 13, 15, 10, 9 };

		for (int i = 0; i < toRemove.length; i++) {
			int rem = toRemove[i];

			assertTrue(binArr.remove(rem));
			assertTrue(binArr2.remove(rem));

			assertFalse(binArr.remove(rem));
			assertFalse(binArr2.remove(rem));

			assertFalse(binArr.contains(rem));
			assertFalse(binArr2.contains(rem));
			assertEquals(values.length - i - 1, binArr.size());
			assertEquals(values.length - i - 1, binArr2.size());
		}
	}
}
