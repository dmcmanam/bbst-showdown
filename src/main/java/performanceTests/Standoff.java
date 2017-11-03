package performanceTests;

import java.io.FileNotFoundException;
import java.util.*;

import bbst_showdown.TreeMapAVL;
import bbst_showdown.TreeMapAVLRB;
import bbst_showdown.TreeMapAVLRec;
import bbst_showdown.TreeMapAVLStack;
import bbst_showdown.TreeMapBST;
import bbst_showdown.TreeMapRedBlack;
import bbst_showdown.TreeMapRAVL;

/**
 * Prints a comparison of tree performance. 
 * 
 * @author David McManamon
 */
public class Standoff {

    public static void main(String[] args) throws FileNotFoundException {
	Map<Integer, Integer> redBlack = new TreeMapRedBlack<>();
	Map<Integer, Integer> avl = new TreeMapAVL<>();
	Map<Integer, Integer> avlRB = new TreeMapAVLRB<>();
	Map<Integer, Integer> ravl = new TreeMapRAVL<>(false);
	Map<Integer, Integer> avlNoParent = new TreeMapAVLStack<>();
	Map<Integer, Integer> bst = new TreeMapBST<>();
	Map<Integer, Integer> avlRec = new TreeMapAVLRec<>();

	List<Map<Integer, Integer>> maps = new ArrayList<Map<Integer, Integer>>();
	maps.add(redBlack); // 0=red-black
	maps.add(avl);      // 1=avl
	maps.add(ravl);	    // 2=ravl
	maps.add(bst);      // 4-bst (no rotations)
	
	// TODO update the integer to choose a different tree implementation
	Map<Integer, Integer> treeMap = maps.get(2);
	
	int mean;
	Integer [] randomInts = readRandomInts();
	
	mean = delete(treeMap, randomInts);
	System.out.println("Delete time: " + mean + "ms, " + treeMap);
	
	mean = insert(treeMap, randomInts);
	System.out.println("Random insert time: " + mean + "ms, " + treeMap);
	
	mean = insertInOrder(treeMap, 100000);
	System.out.println("Sequential insert time: " + mean + "ms, " + treeMap);
    }

    private static Integer[] readRandomInts() throws FileNotFoundException {
	Integer[] v = new Integer[1000000];
	String fileName = "randomInts.txt";
	java.io.File file = new java.io.File(fileName);

	java.util.Scanner inputStream = new java.util.Scanner(file);
	inputStream.nextLine();
	while (inputStream.hasNext()) {
	    String data = inputStream.next();
	    String[] values = data.split(",");

	    for (int i = 0; i < 1000000; i++) {
		v[i] = Integer.parseInt(values[i]);
	    }
	}
	inputStream.close();
	return v;
    }
    
    private static int insertInOrder(Map<Integer, Integer> tree, int nElements) {
	int[] times = new int[5];
	for (int j = 0; j < 5; j++) {
	    long start = System.currentTimeMillis();
	    tree.clear();
	    for (Integer i = 0; i < nElements; i++) {
		tree.put(i, i);
	    }
	    long stop = System.currentTimeMillis();
	    times[j] = (int) (stop - start);
	}

	Arrays.sort(times);

	return (times[1] + times[2] + times[3]) / 3;
    }
    
    private static int delete(Map<Integer, Integer> tree, Integer[] rands) {
	for (int i = 0; i < 100000; i++) {
	    tree.put(rands[i], rands[i]);
	}
	long start = System.currentTimeMillis();
	for (int i = 100000; i > 0 ; i--) {
	    tree.remove(rands[i-1]);
	}
	long stop = System.currentTimeMillis();
	return (int)(stop-start);
    }

    private static int insert(Map<Integer, Integer> tree, Integer[] rands) {
	int [] times = new int[10];
	int x = 0;
	for (int j = 0; j < 10; j++) {
	    long start = System.currentTimeMillis();
	    tree.clear();
	    for (int i = 0; i < 100000; i++) {
		tree.put(rands[x], rands[x]);
		x++;
	    }
	    long stop = System.currentTimeMillis();
	    times[j] = (int) (stop - start);
	}

	Arrays.sort(times);
	
	return (times[2] + times[3] + times[4] + times[5] + times[6] + times[7]) / 6;
    }

    public static long insertDeleteLookup(Map<Integer, Integer> x) {
	long start = System.currentTimeMillis();
	java.util.Random r = new java.util.Random();
	Integer[] inserted = new Integer[1000000];
	for (Integer i = 0; i < 1000000; i++) {
	    if (i < 20000) {
		inserted[i] = i;
		x.put(i, i);
	    } else {
		Integer next = r.nextInt();
		inserted[i] = next;
		x.put(next, next);
	    }
	}

	for (Integer i = 0; i < 500000; i++) {
	    if (i % 2 == 0)
		x.remove(inserted[i]);
	    else
		x.get(inserted[i]);
	}

	long stop = System.currentTimeMillis();
	return stop - start;
    }
}