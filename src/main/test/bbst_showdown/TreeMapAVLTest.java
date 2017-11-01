package bbst_showdown;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TreeMapAVLTest {

    TreeMapAVL<Integer, Integer> x = new TreeMapAVL<>();
    
    @Before
    public void setup(){
	x.clear();
    }
    
    @Test
    public void testInsert100() {
	for (int i=0; i < 100; i++)
	    x.put(i, i);
	assertEquals(100, x.size());
	System.out.println(x.rotations);
    }
    
    @Test
    public void testInsertMany() {
	Integer [] a = {477,1193,2130,398,1393,946,422,1381,1767,830,570,1085,741,598,1658,1801,487,1921,1918,258,135,975,1870};
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
	Integer [] a = {477,1193,2130,398,1393,946,422,1381,1767,830,570,1085,741,598,1658,1801,487,1921,1918,258,135,975,1870};
	for (int i=0; i < a.length; i++)
	    x.put(a[i], a[i]);
	for (int i=a.length-1; i > 0; i--) {
	    x.remove(a[i], a[i]);
	    if (a[i] == 830)
		assertEquals(-1, x.root.left.balance);
	}
	assertEquals(477, (int) x.root.value);
	assertEquals(0, x.root.balance);
	assertNull(x.root.left);
	assertNull(x.root.right);
    }
    
    @Test
    public void testDelete6() {
	for (int i=0; i<6; i++)
	    x.put(i, i);
	for (int i=0; i<6; i++) {
	    System.out.println(x.root + "Deleting:"+ i);
	    x.remove(i);
	}
	assertEquals(0, x.size());
    }
    
    @Test
    public void testDeleteNoRotation() {
	x.put(2, 2);
	x.put(3, 3);
	x.put(1, 1);
	assertEquals(2, (int)x.root.value);
	
	x.remove(3);
	assertEquals(-1, x.root.balance);
	assertEquals(0, x.rotations);
	
	// remove root
	x.remove(2);
	x.remove(1);
	assertEquals(0, x.size());
    }
    
    @Test
    public void testDeleteRoot() {
	x.put(8, 8);
	x.remove(8);
	assertTrue(x.size() == 0);
    }

    @Test
    public void testDeleteFibonacci() {
	x.put(8, 8);
	x.put(5, 5);
	x.put(11, 11);
	// 3,7,10,12
	x.put(3, 3); x.put(7, 7); x.put(10, 10); x.put(12, 12);
	// 2,4,6,9
	x.put(2, 2); x.put(4, 4); x.put(6, 6); x.put(9, 9);
	x.put(1, 1);
	System.out.println("Rotations Before fibonacci remove: " + x.rotations);
	x.remove(12, 12);
	System.out.println("Rotations after fibonacci remove: " + x.rotations);
	
	assertEquals(5, (int) x.root.value);
	assertEquals(2, x.rotations);
	assertEquals(-1, x.root.left.balance);
    }

    @Test
    public void testDeleteRightRotation() {
	x.put(3, 3);
	x.put(2, 2);
	x.put(4, 4);
	x.put(1, 1);

	x.remove(4);
	x.inOrderTraversal(x.root);
    }
    
    @Test
    public void testDeleteOneLeftRightRotation() {
	x.put(10, 10);
	x.put(8, 8);
	x.put(12, 12);
	x.put(9, 9);
	
	x.remove(12);
	
	assertEquals(0, x.root.balance);
	assertEquals(9, (int) x.root.value);
	assertEquals(2, x.rotations);
    }
}