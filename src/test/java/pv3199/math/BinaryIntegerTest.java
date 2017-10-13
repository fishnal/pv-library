package pv3199.math;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BinaryIntegerTest {
	private int add(int... ints) {
		switch (ints.length) {
			case 0: return 0;
			case 1: return ints[0];
			default: {
				BinaryInteger a = new BinaryInteger(ints[0]);
				
				for (int i = 1; i < ints.length; i++) {
					BinaryInteger b = new BinaryInteger(ints[i]);
					a = a.add(b);
				}
				
				return Integer.parseInt(a.toString(), 2);
			}
		}
	}
	
	@Test
	public void test() {
		assertEquals(0, add(0, 0));
		assertEquals(1, add(0, 1));
		assertEquals(1, add(1, 0));
		assertEquals(2, add(1, 1));
		assertEquals(2, add(0, 2));
		assertEquals(2, add(2, 0));
		assertEquals(3, add(1, 2));
		assertEquals(3, add(2, 1));
		assertEquals(3, add(3, 0));
		assertEquals(3, add(0, 3));
		assertEquals(4, add(0, 4));
		assertEquals(4, add(4, 0));
		assertEquals(4, add(1, 3));
		assertEquals(4, add(3, 1));
		assertEquals(4, add(2, 2));
		
		assertEquals(4, add(0, 0, 4));
		assertEquals(4, add(1, 1, 2));
		assertEquals(4, add(2, 2, 0));
		assertEquals(4, add(3, 0, 1));
	}
	
	@Test
	public void randomTest() {
		Random r = new Random(0);
		
		for (int i = 0; i < 100000; i++) {
			int a = r.nextInt(Integer.MAX_VALUE);
			int b = r.nextInt(Integer.MAX_VALUE);
			
			try {
				assertEquals(Math.addExact(a, b), add(a, b));
			} catch (ArithmeticException ae) {
				continue;
			}
		}
	}
}
