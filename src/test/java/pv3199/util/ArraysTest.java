package pv3199.util;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;

public class ArraysTest {
	public final static String DIR = "src/test/java/pv3199/util";
	private final static Class<? extends Throwable> IAE = IllegalArgumentException.class;
	private final static Class<? extends Throwable> CCE = ClassCastException.class;

    class Person implements Comparable<Person> {
        int age;
        String name;

        Person(int age, String name) {
            this.age = age;
            this.name = name;
        }

        public int compareTo(Person p) {
            return this.age - p.age;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Person)) {
                throw new ClassCastException("object not type of Person");
            }

            Person p = (Person) o;
            return this.age == p.age && this.name.equals(p.name);
        }
    }

	@Test
	public void getTest() {
		assertThrows(IAE, () -> Arrays.get(0, 0));
		assertThrows(IAE, () -> Arrays.get(0, singletonList(0)));

		int[] arr = new int[]{ 1, 2, 3, 4, 5 };
		String[] arr2 = new String[] { "test", "world", "hello" };
		int[][] arr3 = new int[][]{ { 1, 2, 3 }, { 4, 5 }, { 6, 7, 8, 9 } };

		assertEquals(3, Arrays.get(arr, 2));
		assertEquals("test", Arrays.get(arr2,0));
		assertEquals(4, Arrays.get(arr3, 1, 0));
		assertEquals(9, Arrays.get(arr3,2, 3));

		assertEquals(3, Arrays.get(arr, singletonList(2)));
		assertEquals("test", Arrays.get(arr2, singletonList(0)));
		assertEquals(4, Arrays.get(arr3, asList(1, 0)));
		assertEquals(9, Arrays.get(arr3, asList(2, 3)));
	}

	@Test
	public void setTest() {
		assertThrows(IAE, () -> Arrays.set(0, 0, 0));
		assertThrows(IAE, () -> Arrays.set(0, 0, singletonList(0)));

		int[] arr = new int[]{ 1, 2, 3, 4, 5 };
		String[] arr2 = new String[] { "test", "world", "hello" };
		int[][] arr3 = new int[][]{ { 1, 2, 3 }, { 4, 5 }, { 6, 7, 8, 9 } };

		Arrays.set(arr, 4, 1);
		Arrays.set(arr2, "test", 1);
		Arrays.set(arr3, new int[100], 0);

		assertEquals(4, arr[1]);
		assertEquals("test", arr2[1]);
		assertArrayEquals(new int[100], arr3[0]);

		Arrays.set(arr, -232, singletonList(4));
		Arrays.set(arr2, "worldy", singletonList(2));
		Arrays.set(arr3, 10, asList(0, 2));

		assertEquals(-232, arr[4]);
		assertEquals("worldy", arr2[2]);
		assertEquals(10, arr3[0][2]);
	}

	@Test
	public void randFillArrayTest() throws Exception {
		assertThrows(IAE, () -> Arrays.randFillArray(0, Arrays.RandomPrimitive.BOOLEAN));

		// reflectively set the seed of the java.util.Random field "RANDOM"
		// in the Arrays class
		Field randomField = Arrays.class.getDeclaredField("RANDOM");
		randomField.setAccessible(true);
		Object randomObject = randomField.get(null);
		Class<?> randomClass = randomObject.getClass();
		Field seedField = randomClass.getDeclaredField("seed");
		seedField.setAccessible(true);
		AtomicLong seed = (AtomicLong) seedField.get(randomObject);
		Method scrambler = randomClass.getDeclaredMethod("initialScramble", Long.TYPE);
		scrambler.setAccessible(true);
		seed.set((long) scrambler.invoke(null, 0));

		boolean[] booleans = new boolean[100];
		char[] chars = new char[100];
		byte[] bytes = new byte[100];
		short[] shorts = new short[100];
		int[] ints = new int[100];
		long[] longs = new long[100];
		float[] floats = new float[100];
		double[] doubles = new double[100];

		Arrays.randFillArray(booleans, Arrays.RandomPrimitive.BOOLEAN);
		Arrays.randFillArray(chars, Arrays.RandomPrimitive.CHAR);
		Arrays.randFillArray(bytes, Arrays.RandomPrimitive.BYTE);
		Arrays.randFillArray(shorts, Arrays.RandomPrimitive.SHORT);
		Arrays.randFillArray(ints, Arrays.RandomPrimitive.INT);
		Arrays.randFillArray(longs, Arrays.RandomPrimitive.LONG);
		Arrays.randFillArray(floats, Arrays.RandomPrimitive.FLOAT);
		Arrays.randFillArray(doubles, Arrays.RandomPrimitive.DOUBLE);

		// read in serialized data to verify the randomly filled arrays
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				DIR + "/Arrays.randFillArray.out"));
		boolean[] serBooleans = (boolean[]) ois.readObject();
		char[] serChars = (char[]) ois.readObject();
		byte[] serBytes = (byte[]) ois.readObject();
		short[] serShorts = (short[]) ois.readObject();
		int[] serInts = (int[]) ois.readObject();
		long[] serLongs = (long[]) ois.readObject();
		float[] serFloats = (float[]) ois.readObject();
		double[] serDoubles = (double[]) ois.readObject();

		assertArrayEquals(serBooleans, booleans);
		assertArrayEquals(serChars, chars);
		assertArrayEquals(serBytes, bytes);
		assertArrayEquals(serShorts, shorts);
		assertArrayEquals(serInts, ints);
		assertArrayEquals(serLongs, longs);
		assertArrayEquals(serFloats, floats, Float.MIN_VALUE);
		assertArrayEquals(serDoubles, serDoubles, Double.MIN_VALUE);


		// do wrong fillings (should throw exception) and cross fillings (should be safe)
		Object[][] arrays = {
				{booleans, "booleans"}, {Arrays.RandomPrimitive.CHAR, Arrays.RandomPrimitive.BYTE, Arrays.RandomPrimitive.SHORT, Arrays.RandomPrimitive.INT, Arrays.RandomPrimitive.LONG, Arrays.RandomPrimitive.FLOAT, Arrays.RandomPrimitive.DOUBLE}, {},
				{chars, "chars"}, {Arrays.RandomPrimitive.BOOLEAN}, {Arrays.RandomPrimitive.BYTE, Arrays.RandomPrimitive.SHORT, Arrays.RandomPrimitive.INT, Arrays.RandomPrimitive.LONG, Arrays.RandomPrimitive.FLOAT, Arrays.RandomPrimitive.DOUBLE},
				{bytes, "bytes"}, {Arrays.RandomPrimitive.BOOLEAN}, {Arrays.RandomPrimitive.BYTE, Arrays.RandomPrimitive.SHORT, Arrays.RandomPrimitive.INT, Arrays.RandomPrimitive.LONG, Arrays.RandomPrimitive.FLOAT, Arrays.RandomPrimitive.DOUBLE},
				{shorts, "shorts"}, {Arrays.RandomPrimitive.BOOLEAN}, {Arrays.RandomPrimitive.BYTE, Arrays.RandomPrimitive.SHORT, Arrays.RandomPrimitive.INT, Arrays.RandomPrimitive.LONG, Arrays.RandomPrimitive.FLOAT, Arrays.RandomPrimitive.DOUBLE},
				{ints, "ints"}, {Arrays.RandomPrimitive.BOOLEAN}, {Arrays.RandomPrimitive.BYTE, Arrays.RandomPrimitive.SHORT, Arrays.RandomPrimitive.INT, Arrays.RandomPrimitive.LONG, Arrays.RandomPrimitive.FLOAT, Arrays.RandomPrimitive.DOUBLE},
				{longs, "longs"}, {Arrays.RandomPrimitive.BOOLEAN}, {Arrays.RandomPrimitive.BYTE, Arrays.RandomPrimitive.SHORT, Arrays.RandomPrimitive.INT, Arrays.RandomPrimitive.LONG, Arrays.RandomPrimitive.FLOAT, Arrays.RandomPrimitive.DOUBLE},
				{floats, "floats"}, {Arrays.RandomPrimitive.BOOLEAN}, {Arrays.RandomPrimitive.BYTE, Arrays.RandomPrimitive.SHORT, Arrays.RandomPrimitive.INT, Arrays.RandomPrimitive.LONG, Arrays.RandomPrimitive.FLOAT, Arrays.RandomPrimitive.DOUBLE},
				{doubles, "doubles"}, {Arrays.RandomPrimitive.BOOLEAN}, {Arrays.RandomPrimitive.BYTE, Arrays.RandomPrimitive.SHORT, Arrays.RandomPrimitive.INT, Arrays.RandomPrimitive.LONG, Arrays.RandomPrimitive.FLOAT, Arrays.RandomPrimitive.DOUBLE}
		};


		for (int i = 0; i + 2 < arrays.length; i += 3) {
			Object arr = arrays[i][0];
			Object name = arrays[i][1];
			Object[] randTypes = arrays[i + 1];

			for (Object randType : randTypes) {
			    assertThrows(CCE, () -> Arrays.randFillArray(arr, (Arrays.RandomPrimitive) randType));
			}

			randTypes = arrays[i + 2];

			for (Object randType : randTypes) {
				try {
					Arrays.randFillArray(arr, (Arrays.RandomPrimitive) randType);
				} catch (ClassCastException cce) {
					fail(String.format("ClassCastException thrown for invocation " +
							"Arrays.randFillArray(%s, %s)", name, ((Enum) randType).name()));
				}
			}
		}

		// test for null arrays in a multi-dimensional array with ALL RandomPrimitive
		// types because it shouldn't matter if the types are compatible, we can't fill
		// a null array
		boolean[][] booleans2 = new boolean[5][];

		for (Arrays.RandomPrimitive rp : Arrays.RandomPrimitive.values()) {
			Arrays.randFillArray(booleans2, rp);
			assertArrayEquals(booleans2, new boolean[5][]);
		}
	}

	@Test
	public void joinArraysTest() {
		assertThrows(IAE, () -> Arrays.joinArrays(0, 0, 0));

		int[] intArr1 = new int[5];
		int[] intArr2 = new int[9];

		Arrays.randFillArray(intArr1, Arrays.RandomPrimitive.INT);
		Arrays.randFillArray(intArr2, Arrays.RandomPrimitive.INT);

		int[] intActual = Arrays.joinArrays(intArr1, intArr2);
		int[] intExpected = new int[intArr1.length + intArr2.length];

		System.arraycopy(intArr1, 0, intExpected, 0, intArr1.length);
		System.arraycopy(intArr2, 0, intExpected, intArr1.length, intArr2.length);
		assertArrayEquals(intExpected, intActual);

		Object[] objArr1 = new Object[(int) (Math.random() * 50)];
		Object[] objArr2 = new Object[(int) (Math.random() * 50)];

		for (int i = 0; i < objArr1.length; i++) {
			objArr1[i] = new Object();
		}

		for (int i = 0; i < objArr2.length; i++) {
			objArr2[i] = new Object();
		}

		Object[] objActual = Arrays.joinArrays(objArr1, objArr2);
		Object[] objExpected = new Object[objArr1.length + objArr2.length];

		System.arraycopy(objArr1, 0, objExpected, 0, objArr1.length);
		System.arraycopy(objArr2, 0, objExpected, objArr1.length, objArr2.length);
		assertArrayEquals(objExpected, objActual);
	}

	@Test
	public void indexOfTest() {
		assertThrows(IAE, () -> Arrays.indexOf(0, 0));

		Object[] arr = {
				1, 2.3d, 5.4f, null, null, new Object[5]
		};

		assertTrue(Arrays.contains(arr, 1));
		assertTrue(Arrays.contains(arr, 2.3d));
		assertTrue(Arrays.contains(arr, null));
		assertTrue(Arrays.contains(arr, arr[5]));
		assertFalse(Arrays.contains(arr, 6));
		assertFalse(Arrays.contains(arr, new Object[5]));

		arr = new Object[][] {
				new Object[]{5, 2},
				null,
				new String[]{"test", null},
				null
		};

		assertTrue(Arrays.contains(arr, 5));
		assertTrue(Arrays.contains(arr, null));
		assertTrue(Arrays.contains(arr, "test"));
		assertFalse(Arrays.contains(arr, "test2"));
	}

	@Test
	public void lastIndexOfTest() {
		assertThrows(IAE, () -> Arrays.lastIndexOf(0, 0));

		Object[] arr = {
				1, 2.3d, 5.4f, null, null, new Object[5]
		};

		assertArrayEquals(new int[]{0}, Arrays.lastIndexOf(arr, 1));
		assertArrayEquals(new int[]{1}, Arrays.lastIndexOf(arr, 2.3d));
		assertArrayEquals(new int[]{5, 4}, Arrays.lastIndexOf(arr, null));
		assertArrayEquals(new int[]{5}, Arrays.lastIndexOf(arr, arr[5]));

		arr = new Object[][] {
				new Object[]{5, 2, 5},
				null,
				new String[]{"test", null}
		};

		assertArrayEquals(new int[]{0, 2}, Arrays.lastIndexOf(arr, 5));
		assertArrayEquals(new int[]{2, 1}, Arrays.lastIndexOf(arr, null));
	}

	@Test
	public void containsTest() {
		assertThrows(IAE, () -> Arrays.contains(0, 0));

		Object[] arr = { 1, 2.3d, 5.4f, null, null, new Object[5] };

		assertArrayEquals(new int[]{0}, Arrays.indexOf(arr, 1));
		assertArrayEquals(new int[]{1}, Arrays.indexOf(arr, 2.3d));
		assertArrayEquals(new int[]{3}, Arrays.indexOf(arr, null));
		assertArrayEquals(new int[]{5}, Arrays.indexOf(arr, arr[5]));

		arr = new Object[][] {
				new Object[]{5, 2},
				null,
				new String[]{"test", null},
				null
		};

		assertArrayEquals(new int[]{0, 0}, Arrays.indexOf(arr, 5));
		assertArrayEquals(new int[]{1}, Arrays.indexOf(arr, null));
	}

	@Test
	public void minMaxTest() {
		assertThrows(IAE, () -> Arrays.max(new Object[0]));
		assertThrows(IAE, () -> Arrays.min(new Object[0]));
		assertThrows(IAE, () -> Arrays.max(0));
		assertThrows(IAE, () -> Arrays.min(0));
		assertThrows(IAE, () -> Arrays.max(new Object[0], null));
		assertThrows(IAE, () -> Arrays.min(new Object[0], null));
		assertThrows(IAE, () -> Arrays.max(0, (a, b) -> 0));
		assertThrows(IAE, () -> Arrays.min(0, (a, b) -> 0));

		Person[] people = {
			new Person(0, "c"),
			new Person(10, "b"),
			new Person(6, "a")
		};
		Comparator<Person> stringComparator = Comparator.comparing(p -> p.name);

		assertEquals(people[1], Arrays.max(people));
		assertEquals(people[0], Arrays.max(people, stringComparator));
		assertEquals(people[0], Arrays.min(people));
		assertEquals(people[2], Arrays.min(people, stringComparator));

		Person[][] peoples = {
			people,
			{ new Person(2, "e"), new Person(13, "g") },
			{ new Person(13921, "\u0000"), new Person(321, "\uffff") }
		};

		assertEquals(peoples[2][0], Arrays.max(peoples));
		assertEquals(peoples[2][1], Arrays.max(peoples, Comparator.<Person, String>comparing(p -> p.name)));
		assertEquals(peoples[0][0], Arrays.min(peoples));
		assertEquals(peoples[2][0], Arrays.min(peoples, stringComparator));
	}

	@Test
	public void sortingTest() {
		assertThrows(IAE, () -> Arrays.sort(0));
		assertThrows(IAE, () -> Arrays.reverseSort(0));
		assertThrows(IAE, () -> Arrays.sort(new Object[0]));
		assertThrows(IAE, () -> Arrays.reverseSort(new Object[0]));
		assertThrows(IAE, () -> Arrays.sort(0, null));
		assertThrows(IAE, () -> Arrays.reverseSort(0, null));
		assertThrows(IAE, () -> Arrays.sort(0, (a, b) -> 0));
		assertThrows(IAE, () -> Arrays.reverseSort(0, (a, b) -> 0));

		Integer[] arr = { 6, 2, 3, 7, 1, 0, 9 };
		Integer[] sorted = { 0, 1, 2, 3, 6, 7, 9 };
		Integer[] reversed = { 9, 7, 6, 3, 2, 1, 0 };

		assertArrayEquals(sorted, (Integer[]) Arrays.sort(arr));
        assertArrayEquals(reversed, (Integer[]) Arrays.reverseSort(arr));

        Person p1 = new Person(0, "c");
        Person p2 = new Person(10, "b");
        Person p3 = new Person(6, "a");
        Comparator<Person> stringComparator = Comparator.comparing(p -> p.name);

        Person[] people = { p1, p2, p3 };
        Person[] peopleSorted = { p1, p3, p2 };
        Person[] peopleReversed = { p2, p3, p1 };
        Person[] stringSorted = { p3, p2, p1 };
        Person[] stringReversed = { p1, p2, p3 };

        assertArrayEquals(peopleSorted, (Person[]) Arrays.sort(people));
        assertArrayEquals(peopleReversed, (Person[]) Arrays.reverseSort(people));
        assertArrayEquals(stringSorted, (Person[]) Arrays.sort(people, stringComparator));
        assertArrayEquals(stringReversed, (Person[]) Arrays.reverseSort(people, stringComparator));
	}

	@Test
    public void castArrayTest() {
        assertThrows(IAE, () -> Arrays.castArray(0, null));

        assertNull(Arrays.castArray(new int[]{}, boolean.class));
        assertNull(Arrays.castArray(new int[]{}, Boolean.class));
        assertNull(Arrays.castArray(new Integer[]{}, boolean.class));
        assertNull(Arrays.castArray(new Integer[]{}, Boolean.class));

        Integer[] intArr = { 1, 2, 3, 4, 5 };
        Byte[] byteArr = { 1, 2, 3, 4, 5 };
        Character[] charArr = { '\u0001', '\u0002', '\u0003', '\u0004', '\u0005' };
        Short[] shortArr = { 1, 2, 3, 4, 5 };
        Long[] longArr = { 1L, 2L, 3L, 4L, 5L };
        Float[] floatArr = { 1f, 2f, 3f, 4f, 5f };
        Double[] doubleArr = { 1d, 2d, 3d, 4d, 5d };
        Object[][] arrays = { intArr, byteArr, charArr, shortArr, longArr, floatArr, doubleArr };
        Class<?>[] wrapTypes = { Integer[].class, Byte[].class, Character[].class,
                Short[].class, Long[].class, Float[].class, Double[].class };

        for (Object[] arr : arrays) {
            for (int j = 0; j < wrapTypes.length; j++) {
                Class<?> type = wrapTypes[j];
                Object[] expected = arrays[j];
                Object[] actual = (Object[]) Arrays.castArray(arr, type);

                assertArrayEquals(expected, actual);
            }
        }

        Object[] objects = { "", "", "" };
        String[] strings = { "", "", "" };

        assertArrayEquals(strings, Arrays.castArray(objects, String[].class));
        assertArrayEquals(objects, Arrays.castArray(strings, Object[].class));

        assertThrows(CCE, () -> Arrays.castArray(objects, Integer[].class));
        assertArrayEquals(new Integer[3], Arrays.castArray(new Object[3], Integer[].class));

        objects[1] = 2;

        assertThrows(CCE, () -> Arrays.castArray(objects, String[].class));
    }

    @Test
    public void cloneTest() {
        assertThrows(IAE, () -> Arrays.clone(0));

        Integer[] intArr = { 0, 1, 2, 3 };
        Object[] objArr = { new Object(), new Object(), new Object() };

        assertNotSameShallowArrays(intArr, Arrays.clone(intArr));
        assertNotSameShallowArrays(objArr, Arrays.clone(objArr));

        Integer[][] int2D = { intArr, intArr, intArr };
        Object[] obj2D = { objArr, objArr, objArr };

        assertNotSameShallowArrays(int2D, Arrays.clone(int2D));
        assertNotSameShallowArrays(obj2D, Arrays.clone(obj2D));
    }

    /**
     * Assert that array references are not the same but the individual elements are the same
     * reference.
     * @param reference the original array.
     * @param clone the cloned array.
     */
    private void assertNotSameShallowArrays(Object reference, Object clone) {
        try {
            int length = Array.getLength(reference);
            assertTrue(clone.getClass().isArray());
            assertEquals(length, Array.getLength(clone));

            for (int i = 0; i < length; i++) {
                Object ri = Array.get(reference, i);
                Object ci = Array.get(clone, i);

                assertNotSameShallowArrays(ri, ci);
            }
        } catch (IllegalArgumentException iae) {
            assertFalse(clone.getClass().isArray());
            assertSame(reference, clone);
        }
    }

    @Test
    public void equalsTest() {
        boolean[] booleans = new boolean[100];
        byte[] bytes = new byte[100];
        char[] chars = new char[100];
        short[] shorts = new short[100];
        int[] ints = new int[100];
        long[] longs = new long[100];
        float[] floats = new float[100];
        double[] doubles = new double[100];

        assertTrue(Arrays.equals(booleans, booleans.clone()));
        assertTrue(Arrays.equals(bytes, bytes.clone()));
        assertTrue(Arrays.equals(chars, chars.clone()));
        assertTrue(Arrays.equals(shorts, shorts.clone()));
        assertTrue(Arrays.equals(ints, ints.clone()));
        assertTrue(Arrays.equals(longs, longs.clone()));
        assertTrue(Arrays.equals(floats, floats.clone()));
        assertTrue(Arrays.equals(doubles, doubles.clone()));
    }

    @Test
    public void deepEqualsTest() {
        Object[][] arr = new Object[5][3];
        Object[][] arr2 = new Object[5][3];

        assertFalse(java.util.Arrays.equals(arr, arr2));
        assertTrue(Arrays.deepEquals(arr, arr2));

        arr[2] = null;
        arr2[2] = null;

        assertFalse(java.util.Arrays.equals(arr, arr2));
        assertTrue(Arrays.deepEquals(arr, arr2));
    }
    
    @Test
	public void reverseTest() {
    	assertThrows(IAE, () -> Arrays.reverse(0));
    	
    	int[] arr = { 5, 3, 2, 0, 7 };
    	
    	Arrays.reverse(arr);
    	
    	assertArrayEquals(new int[]{ 7, 0, 2, 3, 5 }, arr);
    	
    	arr = new int[] { 5, 3, 2, 0 };
    	
    	Arrays.reverse(arr);
    	
    	assertArrayEquals(new int[]{ 0, 2, 3, 5 }, arr);
	}
}