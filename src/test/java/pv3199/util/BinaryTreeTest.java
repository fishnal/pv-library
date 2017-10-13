package pv3199.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BinaryTreeTest {
    @Test
    public void constructorTest() {
        BinaryTree<Integer> bt = new BinaryTree<>();
        assertEquals(0, bt.size());

        bt = new BinaryTree<>(0);
        assertEquals(1, bt.size());
    }

    // add(E) and add(E[])
    @Test
    public void addTest() {
		BinaryTree<Integer> bt = new BinaryTree<>();
		
		int[] values = { 10, 3, 5, 15, 12 };
		
		for (int v : values) {
			bt.add(v);
		}
		
		assertEquals(5, bt.size());
		
		for (int v : values) {
			assertTrue(bt.contains(v));
		}
		
		bt.add(2, 8, 4, 0, 1);
		
		assertFalse(bt.contains(-1));
		
		for (int v : new int[]{ 2, 8, 4, 0, 1 }) {
			assertTrue(bt.contains(v));
		}
    }
    
    // remove(E)
    @Test
	public void removeTest() {
 		BinaryTree<Integer> bt = new BinaryTree<>();
 		
 		bt.add(10, 3, 5, 15, 12, 7);
 		
 		assertTrue(bt.remove(7));
		assertTrue(bt.remove(10));
		assertTrue(bt.remove(12));
		assertTrue(bt.remove(3));
		assertFalse(bt.remove(10));
	}
	
	@Test
	public void otherTests() {
		BinaryTree<Integer> bt = new BinaryTree<>();
		
		bt.add(10, 3, 5, 15, 12, 7);
		
		assertEquals(9, bt.internalPathLength());
		assertEquals(4, bt.internalNodes());
		assertEquals(17, bt.externalPathLength());
		assertEquals(2, bt.leafCount());
		
		BinaryTree<Integer> bt2 = new BinaryTree<>();
		BinaryTree<Integer> bt3 = new BinaryTree<>();
		
		bt2.add(3, 5, 7, 12, 15, 10);
		bt3.add(10, 3, 5, 15, 12, 7);
		
		assertFalse(bt.equals(bt2));
		assertTrue(bt.equals(bt3));
		assertFalse(bt.equals(null));
		assertFalse(bt.equals(0));
		
		BinaryTree<String> btString = new BinaryTree<>();
		
		btString.add("hello", "america", "zoo");
		
		assertFalse(bt.equals(btString));
	}
}
