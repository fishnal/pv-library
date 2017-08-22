package pv3199.lib.java.util;

import static java.util.Arrays.asList;

import static pv3199.lib.java.util.Arrays.RandomPrimitive;

import jdk.internal.org.objectweb.asm.commons.RemappingAnnotationAdapter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Random;

public class ArraysTest {
	// pv3199.lib.java.util.Arrays#get(Object, int...)
	// pv3199.lib.java.util.Arrays#get(Object, Iterator<Integer>)
	@Test
	public void testGet() {
		int[] arr1 = {9, 4, 7};
		for (int i = 0; i < arr1.length; i++) {
			Assert.assertEquals(arr1[i], Arrays.get(arr1, i));
			Assert.assertEquals(arr1[i], Arrays.get(arr1, asList(i).iterator()));
		}
		int[][] arr2 = {{9, 4, 7}, {8, 3, 6}};
		for (int i = 0; i < arr2.length; i++) {
			for (int j = 0; j < arr2.length; j++) {
				Assert.assertEquals(arr2[i][j], Arrays.get(arr2, i, j));
				Assert.assertEquals(arr2[i][j], Arrays.get(arr2, asList(i, j).iterator()));
			}
			Assert.assertArrayEquals(arr2[i], (int[]) Arrays.get(arr2, i));
			Assert.assertArrayEquals(arr2[i], (int[]) Arrays.get(arr2, asList(i).iterator()));
		}
		int[][][] arr3 = {{{9, 4, 7}, {8, 3, 6}, {2, 5}, {1}}, {{5}}, {{1, 9}, {10}, {}}};
		for (int i = 0; i < arr3.length; i++) {
			for (int j = 0; j < arr3[i].length; j++) {
				for (int k = 0; k < arr3[i][j].length; k++) {
					Assert.assertEquals(arr3[i][j][k], Arrays.get(arr3, i, j, k));
					Assert.assertEquals(arr3[i][j][k], Arrays.get(arr3, asList(i, j, k).iterator()));
				}
				Assert.assertArrayEquals(arr3[i][j], (int[]) Arrays.get(arr3, i, j));
				Assert.assertArrayEquals(arr3[i][j], (int[]) Arrays.get(arr3, asList(i, j).iterator()));
			}
			Assert.assertArrayEquals(arr3[i], (int[][]) Arrays.get(arr3, i));
			Assert.assertArrayEquals(arr3[i], (int[][]) Arrays.get(arr3, asList(i).iterator()));
		}
	}
	
	// pv3199.lib.java.util.Arrays#set(Object, int[], Object)
	// pv3199.lib.java.util.Arrays#set(Object, Iterator<Integer>, Object)
	@Test
	public void testSet() {
		int[] arr1 = {9, 4, 7};
		for (int i = 0; i < arr1.length; i++) {
			int prevValue = arr1[i];
			Arrays.set(arr1, new int[]{i}, arr1[i] * 2);
			Assert.assertEquals(prevValue * 2, arr1[i]);
			arr1[i] = prevValue;
			Arrays.set(arr1, asList(i).iterator(), arr1[i] * 2);
			Assert.assertEquals(prevValue * 2, arr1[i]);
		}
		int[][] arr2 = {{9, 4, 7}, {8, 3, 6}};
		for (int i = 0; i < arr2.length; i++) {
			for (int j = 0; j < arr2.length; j++) {
				int prevValue = arr2[i][j];
				Arrays.set(arr2, new int[]{i, j}, arr2[i][j] * 2);
				Assert.assertEquals(prevValue * 2, arr2[i][j]);
				arr2[i][j] = prevValue;
				Arrays.set(arr2, asList(i, j).iterator(), arr2[i][j] * 2);
				Assert.assertEquals(prevValue * 2, arr2[i][j]);
			}
			int[] prevValue = arr2[i];
			Arrays.set(arr2, new int[]{i}, arr2[i].clone());
			Assert.assertNotSame(arr2[i], prevValue);
			arr2[i] = prevValue;
			Arrays.set(arr2, asList(i).iterator(), arr2[i].clone());
			Assert.assertNotSame(arr2[i], prevValue);
		}
		int[][][] arr3 = {{{9, 4, 7}, {8, 3, 6}, {2, 5}, {1}}, {{5}}, {{1, 9}, {10}, {}}};
		for (int i = 0; i < arr3.length; i++) {
			for (int j = 0; j < arr3[i].length; j++) {
				for (int k = 0; k < arr3[i][j].length; k++) {
					int prevValue = arr3[i][j][k];
					Arrays.set(arr3, new int[]{i, j, k}, arr3[i][j][k] * 2);
					Assert.assertEquals(prevValue * 2, arr3[i][j][k]);
					arr3[i][j][k] = prevValue;
					Arrays.set(arr3, asList(i, j, k).iterator(), arr3[i][j][k] * 2);
					Assert.assertEquals(prevValue * 2, arr3[i][j][k]);
				}
				int[] prevValue = arr3[i][j];
				Arrays.set(arr3, new int[]{i, j}, arr3[i][j].clone());
				Assert.assertNotSame(arr3[i][j], prevValue);
				arr3[i][j] = prevValue;
				Arrays.set(arr3, asList(i, j).iterator(), arr3[i][j].clone());
				Assert.assertNotSame(arr3[i][j], prevValue);
			}
			int[][] prevValue = arr3[i];
			Arrays.set(arr3, new int[]{i}, arr3[i].clone());
			Assert.assertNotSame(arr3[i], prevValue);
			arr3[i] = prevValue;
			Arrays.set(arr3, asList(i).iterator(), arr3[i].clone());
			Assert.assertNotSame(arr3[i], prevValue);
		}
	}
	
	@Test
	public void testRandFillArray() {
		// pv3199.lib.java.util.Arrays#randFillArray(Object, RandomPrimitive)
		char[] arrChar = new char[100];
		byte[] arrByte = new byte[100];
		short[] arrShort = new short[100];
		int[] arrInt = new int[100];
		long[] arrLong = new long[100];
		float[] arrFloat = new float[100];
		double[] arrDouble = new double[100];
		
		// randomly fill based on type of array
		Arrays.randFillArray(arrChar, RandomPrimitive.CHAR);
		Arrays.randFillArray(arrByte, RandomPrimitive.BYTE);
		Arrays.randFillArray(arrShort, RandomPrimitive.SHORT);
		Arrays.randFillArray(arrInt, RandomPrimitive.INT);
		Arrays.randFillArray(arrLong, RandomPrimitive.LONG);
		Arrays.randFillArray(arrFloat, RandomPrimitive.FLOAT);
		Arrays.randFillArray(arrDouble, RandomPrimitive.DOUBLE);
		
		// randomly fill based on the base type of the array (randomly fill bytes into int array, etc)
		Arrays.randFillArray(arrShort, RandomPrimitive.BYTE);
		Arrays.randFillArray(arrInt, RandomPrimitive.BYTE);
		Arrays.randFillArray(arrInt, RandomPrimitive.SHORT);
		Arrays.randFillArray(arrLong, RandomPrimitive.BYTE);
		Arrays.randFillArray(arrLong, RandomPrimitive.SHORT);
		Arrays.randFillArray(arrLong, RandomPrimitive.INT);
		Arrays.randFillArray(arrFloat, RandomPrimitive.BYTE);
		Arrays.randFillArray(arrFloat, RandomPrimitive.SHORT);
		Arrays.randFillArray(arrFloat, RandomPrimitive.INT);
		Arrays.randFillArray(arrFloat, RandomPrimitive.LONG);
		Arrays.randFillArray(arrDouble, RandomPrimitive.BYTE);
		Arrays.randFillArray(arrDouble, RandomPrimitive.SHORT);
		Arrays.randFillArray(arrDouble, RandomPrimitive.INT);
		Arrays.randFillArray(arrDouble, RandomPrimitive.LONG);
		Arrays.randFillArray(arrDouble, RandomPrimitive.FLOAT);
	}
	
	@Test
	public void testJoinArrays() {
		// pv3199.lib.java.util.Arrays#joinArrays(T[]...)
		int[] arr1 = {0, 1, 2};
		int[] arr2 = {3, 4};
		Object joined = Arrays.joinArrays(arr1, arr2);
		Assert.assertArrayEquals(new int[]{0, 1, 2, 3, 4}, (int[]) joined);
		int[][] arr3 = {arr1, arr2};
		int[][] arr4 = {{5}, {6, 7, 8, 9}};
		joined = Arrays.joinArrays(arr3, arr4);
		Assert.assertArrayEquals(new int[][]{arr1, arr2, arr4[0], arr4[1]}, (int[][]) joined);
		Number[] arr5 = {new Integer(5), new Double(10)};
		Number[] arr6 = arr5.clone();
		joined = Arrays.joinArrays(arr5, arr6);
		Assert.assertArrayEquals(new Number[]{5, 10d, 5, 10d}, (Number[]) joined);
	}
}
