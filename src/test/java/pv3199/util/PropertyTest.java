package pv3199.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyTest {
	@Test
	public void test() {
		Property<Object> p1 = new Property<>();
		
		assertNull(p1.getValue());
		
		p1.setValue(new Object());
		
		assertNotNull(p1.getValue());
		
		p1.setListener(PropertyListener.BEFORE_ACCESS, () -> System.out.println("Printed before access!"));
		
		p1.getValue();
		System.out.println("Printed after access!\n");
		
		p1.setListener(PropertyListener.BEFORE_ACCESS, null);
		System.out.println("Expected value before write: " + p1.getValue());
		p1.setListener(PropertyListener.BEFORE_WRITE, () -> System.out.println("Value before write: " + p1.getValue()));
		p1.setListener(PropertyListener.AFTER_WRITE, () -> System.out.println("Value after write: " + p1.getValue()));
		
		p1.setValue(new Object());
	}
}
