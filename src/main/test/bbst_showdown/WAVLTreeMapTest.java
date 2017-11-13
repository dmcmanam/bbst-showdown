package bbst_showdown;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class WAVLTreeMapTest {
    
    WAVLTreeMap<Integer, Integer> x = new WAVLTreeMap<>(true);
    
    @Before
    public void setup(){
	x.clear();
    }

    @Test
    public void testTreeHeight() {
	x.put(2, 2);
	assertEquals(0, x.treeHeight());
	x.put(3, 3);
	assertEquals(1, x.treeHeight());
	x.put(1, 1);
	x.put(0, 0);
	assertEquals(2, x.treeHeight());
    }
    
    @Test
    public void testInsertDoNothing() {
	x.put(2, 2);
	x.put(3, 3);
	assertEquals(0, x.rotations);
	assertEquals(2, (int)x.root.value);
	assertEquals(1, x.root.rank);

	x.put(1, 1);
	assertEquals(2, (int)x.root.value);
	
	assertEquals(1, x.root.rank);
	assertEquals(0, x.rotations);
    }
    
    @Test
    public void testInsert100() {
	for (int i=0; i<100; i++)
	    x.put(i, i);
	assertEquals(100, x.size());
    }
    
    @Test
    public void testInsertMany() {
	Integer [] a = {477, 1193, 2130,398,1393,946,422,1381,1767,830,570,1085,741,598,1658,1801,487,1921,1918,258,135,975,1870};
	for (int i=0; i < a.length; i++)
	    x.put(a[i], a[i]);
	assertEquals(1193, (int) x.root.value);
	assertEquals(1767, (int) x.root.right.value);
	assertEquals(1393, (int) x.root.right.left.value);
	assertEquals(1921, (int) x.root.right.right.value);
	assertEquals(1870, (int) x.root.right.right.left.value);
	assertEquals(1801, (int) x.root.right.right.left.left.value);
	assertEquals(2130, (int) x.root.right.right.right.value);
    }
    
    @Test
    public void testDeleteMany() {
	Integer [] a = {477,1193,2130,398,1393,946,422,1381,1767,830,570,1085,741,598,1658,1801,487};//,1921,1918,258,135,975,1870};
	for (int i=0; i < a.length; i++)
	    x.put(a[i], a[i]);
	for (int i=a.length-1; i > 0; i--) {
	    System.out.println("Deleting:" + i + " value:" + a[i]);
	    x.remove(a[i], a[i]);
	    
	}
	assertEquals(477, (int) x.root.value);
	assertEquals(0, x.root.rank);
	assertNull(x.root.left);
	assertNull(x.root.right);
    }
    
    @Test
    public void testInsertOneLeftRotation() {
	x.put(1, 1);
	x.put(2, 2);
	x.put(3, 3);

	assertEquals(1, x.root.rank);
	assertEquals(2, (int) x.root.value);
	assertEquals(0, x.root.right.rank);
    }

    @Test
    public void testInsertTwoLeftRotations() {
	x.put(1, 1);
	x.put(2, 2);
	x.put(3, 3);
	x.put(4, 4);
	x.put(5, 5);

	assertEquals(2, x.root.rank);
	assertEquals(2, (int) x.root.value);
	assertEquals(2, x.rotations);
	assertEquals(1, x.root.right.rank);
	assertEquals(0, x.root.left.rank);
    }

    @Test
    public void testInsertThreeLeftRotations() {
	x.put(1, 1);
	x.put(2, 2);
	x.put(3, 3);
	x.put(4, 4);
	x.put(5, 5);
	x.put(6, 6);

	assertEquals(3, x.rotations);
	assertEquals(4, (int) x.root.value);
	assertEquals(2, x.root.rank);
	assertTrue(x.root.right.rank == 1 && x.root.left.rank == 1);
    }

    @Test
    public void testInsertLeftRightRotation() {
	x.put(3, 3);
	x.put(1, 1);
	x.put(2, 2);

	assertEquals(2, x.rotations);
	assertEquals(2, (int) x.root.value);
	assertEquals(1, x.root.rank);
    }

    @Test
    public void testInsertRightLeftRotation() {
	x.put(3, 3);
	x.put(6, 6);
	x.put(4, 4);

	assertEquals(2, x.rotations);
	assertEquals(4, (int) x.root.value);
	assertEquals(1, x.root.rank);
    }

    @Test
    public void testInsertBuildFibonacciTree() {
	x.put(8, 8);
	x.put(5, 5);
	x.put(11, 11);
	// 3,7,10,12
	x.put(3, 3); x.put(7, 7); x.put(10, 10); x.put(12, 12);
	// 2,4,6,9
	x.put(2, 2); x.put(4, 4); x.put(6, 6); x.put(9, 9);
	x.put(1, 1);
	System.out.println("Rotations: " + x.rotations);
	assertEquals(0, x.rotations);
    }
    
    @Test
    public void testInsertFibAfterDelete() {
	x.put(8, 8); // root
	x.put(5, 5); x.put(11, 11);
	// 3,7,10,12
	x.put(3, 3); x.put(7, 7); x.put(10, 10); x.put(12, 12);
	// 2,4,6,9
	x.put(2, 2); x.put(4, 4); x.put(6, 6); x.put(9, 9);
	x.put(1, 1);

	x.remove(12, 12);
	//TODO
    }
    
    @Test
    public void testInsert6() {
	for (int i=0; i<6; i++)
	    x.put(i, i);
	assertEquals(3, x.rotations);
	assertEquals(3, (int) x.root.value);
	x.inOrderTraversal(x.root);
    }

    @Test
    public void testInsertTwoRightRotations() {
	x.put(5, 5);
	x.put(4, 4);
	x.put(3, 3);
	x.put(2, 4);
	x.put(1, 1);

	assertEquals(2, x.rotations);
	assertEquals(2, x.root.rank);
	assertEquals(4, (int) x.root.value);
	assertEquals(0, x.root.right.rank);
	assertEquals(1, x.root.left.rank);
    }
    
    @Test
    public void testDeleteNoRotation() {
	x.put(2, 2);
	x.put(3, 3);
	x.put(1, 1);
	assertEquals(2, (int)x.root.value);
	
	x.remove(3);
	assertEquals(1, x.root.rank);
	assertEquals(0, x.rotations);
	
	// remove root
	x.remove(2);
	x.remove(1);
	assertEquals(0, x.size());
    }
    
    @Test
    public void testDeleteNoRotationLeftTallDecrementRank() {
	x.put(2, 2);
	x.put(3, 3);
	x.put(1, 1);
	x.put(0, 0);
	assertEquals(2, (int)x.root.value);
	
	x.remove(0);
	/*
	 2
	1 3
	 */
	assertEquals(1, x.root.rank);
	assertEquals(0, x.rotations);
    }
    
    @Test
    public void testDeleteOneRightRotation() {
	x.put(10, 10);
	x.put(8, 8); x.put(12, 12);
	x.put(6, 6);
	
	x.remove(12);
	/*
	  8
	6  10
	 */
	assertEquals(1, x.rotations);
	assertEquals(1, x.root.rank);
	assertEquals(8, (int) x.root.value);
    }
    
    @Test
    public void testDeleteOneRightRotationSiblingBalanced() {
	x.put(10, 10);
	x.put(8, 8);x.put(12, 12);
	x.put(6,6);x.put(9,9);
	
	assertEquals(0, x.rotations);
	
	x.remove(12);
	/* after
	 8
       6   10
          9
	 */
	assertEquals(2, x.root.rank);
	assertEquals(6, (int) x.root.left.value);
	assertEquals(1, x.root.right.rank);
	assertEquals(0, x.root.left.rank);
	assertEquals(8, (int) x.root.value);
	assertEquals(1, x.rotations);
    }
    
    @Test
    public void testDeleteOneLeftRightRotation() {
	x.put(10, 10);
	x.put(8, 8);
	x.put(12, 12);
	x.put(9, 9);
	
	x.remove(12);
	/*
	  9
	8  10
	 */
	
	assertEquals(0, x.root.right.rank);
	assertEquals(0, x.root.left.rank);
	assertEquals(1, x.root.rank);
	assertEquals(9, (int) x.root.value);
	assertEquals(2, x.rotations);
    }
    
    @Test
    public void testDelete5() {
	for (int i=0; i<6; i++)
	    x.put(i, i);
	for (int i=0; i<5; i++) {
	    System.out.println("Root:" + x.root + " Deleting:"+ i);
	    x.remove(i);
	}
	assertEquals(1, x.size());
	assertEquals(0, x.root.rank);
    }
    
    @Test
    public void testDeleteFibonacciTree() {
	x.put(8, 8); // root
	x.put(5, 5); x.put(11, 11);
	// 3,7,10,12
	x.put(3, 3); x.put(7, 7); x.put(10, 10); x.put(12, 12);
	// 2,4,6,9
	x.put(2, 2); x.put(4, 4); x.put(6, 6); x.put(9, 9);
	x.put(1, 1);
	System.out.println("Rotations before remove 12 from Fibonacci: " + x.rotations);
	x.remove(12, 12);
	System.out.println("Rotations after remove 12: " + x.rotations);
	System.out.println("ROOT:"+ x.root);
	assertEquals(2, x.rotations);
	assertEquals(5, (int) x.root.value);
	
	x.remove(4);
	assertEquals(2, (int) x.root.left.value);
	assertEquals(1, x.root.left.rank);
	x.inOrderTraversal(x.root);
	
    }
}