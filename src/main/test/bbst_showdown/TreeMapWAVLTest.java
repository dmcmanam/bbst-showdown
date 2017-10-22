package bbst_showdown;

import static org.junit.Assert.*;

import org.junit.Test;

public class TreeMapWAVLTest {

    @Test
    public void testTreeHeight() {
	TreeMapWAVL<Integer, Integer> x = new TreeMapWAVL<>();
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
	TreeMapWAVL<Integer, Integer> x = new TreeMapWAVL<>();
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
	TreeMapWAVL<Integer, Integer> x = new TreeMapWAVL<>();
	for (int i=0; i<100; i++)
	    x.put(i, i);
	assertEquals(100, x.size());
    }
    
    @Test
    public void testInsertOneLeftRotation() {
	TreeMapWAVL<Integer, Integer> x = new TreeMapWAVL<>();
	x.put(1, 1);
	x.put(2, 2);
	x.put(3, 3);

	assertEquals(1, x.root.rank);
	assertEquals(2, (int) x.root.value);
	assertEquals(0, x.root.right.rank);
    }

    @Test
    public void testInsertTwoLeftRotations() {
	TreeMapWAVL<Integer, Integer> x = new TreeMapWAVL<>();
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
	TreeMapWAVL<Integer, Integer> x = new TreeMapWAVL<>();
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
	TreeMapWAVL<Integer, Integer> x = new TreeMapWAVL<>();
	x.put(3, 3);
	x.put(1, 1);
	x.put(2, 2);

	assertEquals(2, x.rotations);
	assertEquals(2, (int) x.root.value);
	assertEquals(1, x.root.rank);
    }

    @Test
    public void testInsertRightLeftRotation() {
	TreeMapWAVL<Integer, Integer> x = new TreeMapWAVL<>();
	x.put(3, 3);
	x.put(6, 6);
	x.put(4, 4);

	assertEquals(2, x.rotations);
	assertEquals(4, (int) x.root.value);
	assertEquals(1, x.root.rank);
    }

    @Test
    public void testInsertBuildFibonacciTree() {
	TreeMapWAVL<Integer, Integer> x = new TreeMapWAVL<>();
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
    public void testInsert6() {
	TreeMapWAVL<Integer, Integer> x = new TreeMapWAVL<>();
	for (int i=0; i<6; i++)
	    x.put(i, i);
	assertEquals(3, x.rotations);
	assertEquals(3, (int) x.root.value);
	x.inOrderTraversal(x.root);
    }

    @Test
    public void testInsertTwoRightRotations() {
	TreeMapWAVL<Integer, Integer> x = new TreeMapWAVL<>();
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
    public void testDeleteDoNothing() {
	TreeMapWAVL<Integer, Integer> x = new TreeMapWAVL<>();
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
    public void testDeleteOneRightRotation() {
	TreeMapWAVL<Integer, Integer> x = new TreeMapWAVL<>();
	x.put(10, 10);
	x.put(8, 8);
	x.put(12, 12);
	x.put(6, 6);
	
	x.remove(12);
	
	assertEquals(1, x.root.rank);
	assertEquals(8, (int) x.root.value);
	assertEquals(1, x.rotations);
    }
    
    @Test
    public void testDeleteOneLeftRightRotation() {
	TreeMapWAVL<Integer, Integer> x = new TreeMapWAVL<>();
	x.put(10, 10);
	x.put(8, 8);
	x.put(12, 12);
	x.put(9, 9);
	
	x.remove(12);
	
	assertEquals(1, x.root.rank);
	assertEquals(9, (int) x.root.value);
	assertEquals(2, x.rotations);
    }
    
    @Test
    public void testDelete6() {
	TreeMapWAVL<Integer, Integer> x = new TreeMapWAVL<>();
	for (int i=0; i<6; i++)
	    x.put(i, i);
	for (int i=0; i<6; i++) {
	    System.out.println(x.root + "Deleting:"+ i);
	    x.remove(i);
	}
	assertEquals(0, x.size());
    }
}