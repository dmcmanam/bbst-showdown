package bbst_showdown;

import java.util.Map;
import java.util.TreeMap;

/**
 * Prints a comparison of tree performance.  If you think red-black trees are better than AVL trees execute this code.
 * 
 * @author David McManamon
 */
public class Standoff {

	public static void main(String [] args) {
		TreeMapAVL<Integer, Integer> avl = new TreeMapAVL<>();
		TreeMap<Integer, Integer> redBlack = new TreeMap<>();
		
		java.util.Random r = new java.util.Random();
		Integer [] rands = new Integer[500000];
		for (int i=0; i < rands.length; i++) {
			Integer next = r.nextInt(Integer.MAX_VALUE);
			rands[i] = next;
		}
		
		System.out.println("Results for randomly inserting " + rands.length + " integers:");
		int mean1, mean2 = 0;
		int runs = 0;
		do {
			runs++;
			mean1 = insertRandomOrder(avl, rands);
			mean2 = insertRandomOrder(avl, rands);
		} while (Math.abs((mean1-mean2) / (double) mean1) > 0.03);
		System.out.println("  Mean AVL insertion time: " + mean1 + " and " + mean2 + "ms, runs to converge:" + runs);
		runs = 0;
		
		do {
			runs++;
			mean1 = insertRandomOrder(redBlack, rands);
			mean2 = insertRandomOrder(redBlack, rands);
		} while (Math.abs((mean1-mean2) / (double) mean1) > 0.03);
		System.out.println("  Mean red-black insertion time: " + mean1 + " and " + mean2 + "ms, runs to converge:" + runs);
		
		
	}

	private static int insertRandomOrder(Map<Integer, Integer> tree, Integer [] rands) {
		// warmup
		for (int i=0; i < rands.length/5; i++) {
			tree.put(rands[i], rands[i]);
		}
		
		long start = System.currentTimeMillis();
		for (int run=0; run < 7; run++) {
			tree.clear();
			for (int i=0; i < rands.length; i++) {
				tree.put(rands[i], rands[i]);
			}
		}
		long stop = System.currentTimeMillis();
		
		return (int)(stop - start) / 7;
	}

	public static long insertInOrder(Map<Integer, Integer> x) {
		long start = System.currentTimeMillis();
		for(Integer i=0; i < 10000000; i++) {
			x.put(i, i);
		}
		long stop = System.currentTimeMillis();
		return stop - start;
	}
	
	public static long insertDeleteLookup(Map<Integer, Integer> x) {
		long start = System.currentTimeMillis();
		java.util.Random r = new java.util.Random();
		Integer [] inserted = new Integer[1000000];
		for(Integer i=0; i < 1000000; i++) {
			if (i < 20000) {
				inserted[i] = i;
				x.put(i, i);
			} else {
				Integer next = r.nextInt();
				inserted[i] = next;
				x.put(next, next);
			}
		}
		
		for(Integer i=0; i < 500000; i++) {
			if (i % 2 == 0)
				x.remove(inserted[i]);
			else
				x.get(inserted[i]);
		}
		
		long stop = System.currentTimeMillis();
		return stop - start;
	}
}
