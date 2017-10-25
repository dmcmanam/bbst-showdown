package bbst_showdown;

import static org.junit.Assert.*;

import org.junit.Test;

public class TreeMapAVLRBTest {

    @Test
    public void testTreeHeight() {
	TreeMapAVLRB<Integer, Integer> x = new TreeMapAVLRB<>();
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
	TreeMapAVLRB<Integer, Integer> x = new TreeMapAVLRB<>();
	x.put(2, 2);
	x.put(3, 3);
	assertEquals(0, x.rotations);
	assertEquals(2, (int)x.root.value);

	x.put(1, 1);
	assertEquals(2, (int)x.root.value);
	
	assertEquals(0, x.rotations);
	assertTrue(x.root.right.deltaR == x.root.left.deltaR);
    }
    
    @Test
    public void testInsert100() {
	TreeMapAVLRB<Integer, Integer> x = new TreeMapAVLRB<>();
	for (int i=0; i < 100; i++)
	    x.put(i, i);
	assertEquals(100, x.size());
	assertEquals(93, x.rotations);
    }
    
    @Test
    public void testInsert6() {
        TreeMapAVLRB<Integer, Integer> x = new TreeMapAVLRB<>();
        for (int i=0; i<6; i++) 
            x.put(i, i);
        assertEquals(3, x.rotations);
        assertEquals(3, (int) x.root.value);
        x.inOrderTraversal(x.root);
    }

    @Test
    public void testInsertOneLeftRotation() {
	TreeMapAVLRB<Integer, Integer> x = new TreeMapAVLRB<>();
	x.put(1, 1);
	x.put(2, 2);
	x.put(3, 3);

	assertEquals(1, x.rotations);
	assertEquals(TreeMapAVLRB.ONE, x.root.right.deltaR);
	assertEquals(TreeMapAVLRB.ONE, x.root.left.deltaR);
	assertEquals(2, (int) x.root.value);
    }

    /*          2
		                     
           /         \                

        1               4        

                     /     \          
		        
                   3         5
*/
    @Test
    public void testInsertTwoLeftRotations() {
	TreeMapAVLRB<Integer, Integer> x = new TreeMapAVLRB<>();
	x.put(1, 1);
	x.put(2, 2);
	x.put(3, 3);
	x.put(4, 4);
	x.put(5, 5);

	assertEquals(TreeMapAVLRB.TWO, x.root.left.deltaR);
	assertEquals(TreeMapAVLRB.ONE, x.root.right.left.deltaR);
	assertEquals(TreeMapAVLRB.ONE, x.root.right.deltaR);
	assertEquals(2, (int) x.root.value);
	assertEquals(2, x.rotations);
    }

    /*
                4                      

           /         \                

        2               5        

     /     \               \          

   1         3               6
     */
    @Test
    public void testInsertThreeLeftRotations() {
	TreeMapAVLRB<Integer, Integer> x = new TreeMapAVLRB<>();
	x.put(1, 1);
	x.put(2, 2);
	x.put(3, 3);
	x.put(4, 4);
	x.put(5, 5);
	
	assertEquals(TreeMapAVLRB.TWO, x.root.left.deltaR);
	x.put(6, 6);

	assertEquals(3, x.rotations);
	assertEquals(4, (int) x.root.value);
	assertEquals(TreeMapAVLRB.ONE, x.root.right.deltaR);
	assertEquals(TreeMapAVLRB.ONE, x.root.right.right.deltaR);
	assertEquals(TreeMapAVLRB.ONE, x.root.left.deltaR);
	assertEquals(TreeMapAVLRB.ONE, x.root.left.left.deltaR);
	assertEquals(TreeMapAVLRB.ONE, x.root.left.right.deltaR);
    }

    @Test
    public void testInsertLeftRightRotation() {
	TreeMapAVLRB<Integer, Integer> x = new TreeMapAVLRB<>();
	x.put(3, 3);
	x.put(1, 1);
	x.put(2, 2);

	assertEquals(2, x.rotations);
	assertEquals(2, (int) x.root.value);
	assertEquals(TreeMapAVLRB.ONE, x.root.right.deltaR);
	assertEquals(TreeMapAVLRB.ONE, x.root.left.deltaR);
    }

    @Test
    public void testInsertRightLeftRotation() {
	TreeMapAVLRB<Integer, Integer> x = new TreeMapAVLRB<>();
	x.put(3, 3);
	x.put(6, 6);
	x.put(4, 4);

	assertEquals(2, x.rotations);
	assertEquals(4, (int) x.root.value);
	assertEquals(TreeMapAVLRB.ONE, x.root.right.deltaR);
	assertEquals(TreeMapAVLRB.ONE, x.root.left.deltaR);
    }
    
    @Test
    public void testInsertRightLeftRotation2() {
	TreeMapAVLRB<Integer, Integer> x = new TreeMapAVLRB<>();
	x.put(1921, 1921);
	x.put(1801, 1801);
	x.put(2130, 2130);
	x.put(1918, 1918);
	x.put(1870, 1870);

	assertEquals(2, x.rotations);
	assertEquals(1921, (int) x.root.value);
	assertEquals(TreeMapAVLRB.TWO, x.root.right.deltaR);
    }

/*
          8
     5        11
   3   7    10   12
 2  4 6    9
1
 
 */
    @Test
    public void testInsertBuildFibonacciTree() {
	TreeMapAVLRB<Integer, Integer> x = new TreeMapAVLRB<>();
	x.put(8, 8);
	x.put(5, 5); x.put(11, 11);
	// 3,7,10,12
	x.put(3, 3); x.put(7, 7); x.put(10, 10); x.put(12, 12);
	// 2,4,6,9
	x.put(2, 2); x.put(4, 4); x.put(6, 6); x.put(9, 9);
	x.put(1, 1);
	System.out.println("Rotations: " + x.rotations);
	assertEquals(0, x.rotations);
	x.inOrderTraversal(x.root);
    }
    
    @Test
    public void testInsertTwoRightRotations() {
	TreeMapAVLRB<Integer, Integer> x = new TreeMapAVLRB<>();
	x.put(5, 5);
	x.put(4, 4);
	x.put(3, 3);
	x.put(2, 4);
	x.put(1, 1);

	assertEquals(2, x.rotations);
	assertEquals(4, (int) x.root.value);
	assertEquals(TreeMapAVLRB.TWO, x.root.right.deltaR);
	assertEquals(TreeMapAVLRB.ONE, x.root.left.left.deltaR);
	assertEquals(TreeMapAVLRB.ONE, x.root.left.deltaR);
    }
    
    @Test
    public void testInsertMany() {
	TreeMapAVLRB<Integer, Integer> x = new TreeMapAVLRB<>();
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
}