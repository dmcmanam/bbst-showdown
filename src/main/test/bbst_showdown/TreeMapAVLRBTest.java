package bbst_showdown;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TreeMapAVLRBTest {
    TreeMapAVLRB<Integer, Integer> x = new TreeMapAVLRB<>();
    
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

	x.put(1, 1);
	assertEquals(2, (int)x.root.value);
	
	assertEquals(0, x.rotations);
	assertTrue(x.root.right.deltaR == x.root.left.deltaR);
    }
    
    @Test
    public void testInsert100() {
	for (int i=0; i < 100; i++)
	    x.put(i, i);
	assertEquals(100, x.size());
	assertEquals(93, x.rotations);
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
    public void testInsertOneLeftRotation() {
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
	x.put(1, 1);
	x.put(2, 2);
	x.put(3, 3);
	x.put(4, 4);
	x.put(5, 5);
	assertEquals(5, (int) x.root.right.right.value);
	System.out.println("Inserting 6");
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
	x.put(8, 8);
	x.put(5, 5); x.put(11, 11); 
	// 3,7,10,12
	x.put(3, 3); x.put(7, 7); x.put(10, 10); x.put(12, 12);
	// 2,4,6,9
	x.put(2, 2); assertEquals(0, x.rotations);
	x.put(4, 4); assertEquals(0, x.rotations);
	x.put(6, 6); assertEquals(0, x.rotations);
	x.put(9, 9); assertEquals(0, x.rotations);
	x.put(1, 1);
	System.out.println("Rotations: " + x.rotations);
	assertEquals(0, x.rotations);
	x.inOrderTraversal(x.root);
    }
    
    @Test
    public void testInsertMaintainDelta2Parent() {
	x.put(20, 20);
	x.put(10, 10); x.put(40, 40);
	x.put(5, 5); x.put(15, 15); x.put(30, 30); x.put(80, 80);
	x.put(0, 0); x.put(7, 7); x.put(12, 12); x.put(60, 60); x.put(160, 160);
	x.put(8, 8);
	assertEquals(TreeMapAVLRB.TWO, x.root.right.deltaR);
	x.put(100, 100);
	assertEquals(TreeMapAVLRB.TWO, x.root.right.deltaR);
    }
    
    @Test
    public void testInsertRemoveDelta2Child() {
	x.put(1767, 1767);
	x.put(1658, 1658);
	x.put(1921, 1921);
	x.put(1801, 1801);
	x.put(2130, 2130); // temporary delta 2 removed by rotations when insert 1918
	System.out.println("Inserting 1918");
	x.put(1918, 1918);
	
	assertEquals(1801, (int) x.root.value);
	assertEquals(TreeMapAVLRB.ONE, x.root.right.right.deltaR);
    }
    
    @Test
    public void testInsertTwoRightRotations() {
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
	Integer [] a = {1493,477,1193,2130,398,1393,946,422,1381,1767,830,570,1085,741,598,1658,1801,487,1921,
		1918,258,135,975,1870,1655,1585,1935,271,1969,1313,1290,162,1485,26,86,244,1140};
	for (int i=0; i < a.length; i++) {
	    System.out.println("INSERT:" + i +"=" + a[i]);
	    x.put(a[i], a[i]);
	    //assertTrue(x.identicalTrees(x.root, y.root));
	}
	assertEquals(1193, (int) x.root.value);
	
    }
}