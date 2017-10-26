package performanceTests;

import java.io.FileNotFoundException;
import java.util.*;

import bbst_showdown.TreeMapAVL;
import bbst_showdown.TreeMapAVLRB;
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
	Map<Integer, Integer> avl = new TreeMapAVL<>();
	Map<Integer, Integer> avlRB = new TreeMapAVLRB<>();
	Map<Integer, Integer> wavl = new TreeMapWAVL<>();
	Map<Integer, Integer> avlNoParent = new TreeMapAVLStack<>();
	Map<Integer, Integer> bst = new TreeMapBST<>();
	Map<Integer, Integer> avlRec = new TreeMapAVLRec<>();

	List<Map<Integer, Integer>> maps = new ArrayList<Map<Integer, Integer>>();
	maps.add(redBlack); // 0=red-black
	maps.add(avl);      // 1=avl
	maps.add(avlRB);	    // 2=avl rank balanced
	maps.add(wavl);	    // 3=wavl
	
	// TODO update the integer to choose a different tree implementation
	Map<Integer, Integer> treeMap = maps.get(0);
	
	int mean;
	mean = insertInOrder(treeMap, 500000);
	System.out.println("Sequential insert time: " + mean + "ms, " + treeMap);

	Integer [] randomInts = readRandomInts();
	mean = insert(treeMap, randomInts, 500000);
	System.out.println("Random insert time: " + mean + "ms, " + treeMap);
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

    private static int insert(Map<Integer, Integer> tree, Integer[] rands, int elementsToInsert) {
	int [] times = new int[7];
	for (int j = 0; j < 7; j++) {
	    long start = System.currentTimeMillis();
	    for (int run = 0; run < 1; run++) {
		tree.clear();
		for (int i = 0; i < elementsToInsert; i++) {
		    tree.put(rands[i], rands[i]);
		}
	    }
	    long stop = System.currentTimeMillis();
	    times[j] = (int) (stop-start);
	}
	
	Arrays.sort(times);
	
	return (times[2] + times[3] + times[4]) / 3;
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