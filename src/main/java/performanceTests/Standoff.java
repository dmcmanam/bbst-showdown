package performanceTests;

import java.io.FileNotFoundException;
import java.util.*;

import bbst_showdown.TreeMapAVL;
import bbst_showdown.TreeMapAVLRec;
import bbst_showdown.TreeMapAVLStack;
import bbst_showdown.TreeMapBST;
import bbst_showdown.TreeMapRedBlack;
import bbst_showdown.TreeMapWAVL;

/**
 * Prints a comparison of tree performance. 
 * 
 * @author David McManamon
 */
public class Standoff {

    public static void main(String[] args) throws FileNotFoundException {
	Map<Integer, Integer> redBlack = new TreeMapRedBlack<>();
	TreeMapAVL<Integer, Integer> avl = new TreeMapAVL<>();
	TreeMapWAVL<Integer, Integer> wavl = new TreeMapWAVL<>();
	TreeMapAVLStack<Integer, Integer> avlNoParent = new TreeMapAVLStack<>();
	TreeMapBST<Integer, Integer> bst = new TreeMapBST<>();
	TreeMapAVLRec<Integer, Integer> avlRec = new TreeMapAVLRec<>();

	List<Map<Integer, Integer>> maps = new ArrayList<Map<Integer, Integer>>();
	maps.add(redBlack); // 0=red-black
	maps.add(avl);      // 1=avl
	maps.add(wavl);	    // 2=wavl
	
	// TODO update the integer to choose a different tree implementation
	Map<Integer, Integer> treeMap = maps.get(0);
	
	System.out.println("Results for inserting 1 million random integers: ");

	Integer [] randomInts = readRandomInts();
	Integer mean = insert(treeMap, randomInts);
	System.out.println("  Mean insertion time: " + mean + "ms, " + treeMap);
	
	System.out.println("Results for inserting 300,000 sequential integers: ");
	mean = insertInOrder(treeMap, 1000000);
	System.out.println("  Mean insertion time: " + mean + "ms, " + treeMap);
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
    
    private static int insertInOrder(Map<Integer, Integer> tree, int elements) {
	long start = System.currentTimeMillis();
	tree.clear();
	for (Integer i=0; i < elements; i++) {
	    tree.put(i,i);
	}
	long stop = System.currentTimeMillis();

	return (int) (stop - start);
    }

    private static int insert(Map<Integer, Integer> tree, Integer[] rands) {
	long start = System.currentTimeMillis();
	for (int run = 0; run < 1; run++) {
	    tree.clear();
	    for (int i = 0; i < rands.length; i++) {
		tree.put(rands[i], rands[i]);
	    }
	}
	long stop = System.currentTimeMillis();

	return (int) (stop - start) / 1;
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