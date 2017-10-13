package pv3199.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PolarCoordinateTest {
	@Test
	public void test() {
		PolarCoordinate pc = new PolarCoordinate(0, 0);
		
		assertEquals(0d, pc.r);
		assertEquals(0d, pc.theta);
		assertEquals(0d, pc.x);
		assertEquals(0d, pc.y);
		
		double theta = Math.PI * .45;
		PolarCoordinate pc2 = new PolarCoordinate(3.5, theta);
		
		assertEquals(3.5, pc2.r);
		assertEquals(theta, pc2.theta);
		assertEquals(3.5 * Math.cos(theta), pc2.x);
		assertEquals(3.5 * Math.sin(theta), pc2.y);
		
		assertEquals(new ComplexNumber(0, 0), pc.getValue());
		assertEquals(new ComplexNumber(pc2.x, pc2.y), pc2.getValue());
		
		assertTrue(pc.compareTo(pc2) < 0);
		assertTrue(pc.compareTo(pc) == 0);
		assertTrue(pc2.compareTo(pc) > 0);
		
		assertEquals(String.format("(%f,%f)", pc.r, pc.theta), pc.toString());
		assertEquals(String.format("(%f,%f)", 3.5, theta), pc2.toString());
	}
}
