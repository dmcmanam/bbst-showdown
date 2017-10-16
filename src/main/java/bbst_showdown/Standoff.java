package bbst_showdown;

import java.util.Arrays;
import java.util.Map;

/**
 * Prints a comparison of tree performance. 
 * 
 * @author David McManamon
 */
public class Standoff {

	public static void main(String [] args) {
		/**
		 * This code represents my attempts to understand the performance of red-black & AVL trees, not my best effort at clean code.
		 * To see a fair comparison, 'runs to converge' should be identical or nearly identical.
		 * light refactor coming soon.
		 */
		TreeMapAVL<Integer, Integer> avl = new TreeMapAVL<>();
		TreeMapRB<Integer, Integer> redBlack = new TreeMapRB<>();
		TreeMapBST<Integer, Integer> bst = new TreeMapBST<>();
		
		java.util.Random r = new java.util.Random();
		Integer [] rands = new Integer[ (int) (Math.pow(2, 18) - 1)];
		for (int i=0; i < rands.length; i++) {
			Integer next = r.nextInt(Integer.MAX_VALUE);
			rands[i] = next;
		}
		Integer [] sorted = Arrays.copyOf(rands, rands.length);
		Arrays.sort(sorted);
		
		
		System.out.println("Results for inserting " + rands.length + " random integers (height 18 for a complete BST) -");
		int mean1, mean2 = 0;
		int runs = 0;
		
		runs = 0;
		do {
			runs++;
			mean1 = insert(redBlack, rands);
			mean2 = insert(redBlack, rands);
		} while (Math.abs((mean1-mean2) / (double) mean1) > 0.04);
		System.out.println("  Mean insertion time: " + mean1 + "ms and " + mean2 + "ms, runs to converge:" + runs + ". " + redBlack);
		
		runs = 0;
		do {
			runs++;
			mean1 = insert(avl, rands);
			mean2 = insert(avl, rands);
		} while (Math.abs((mean1-mean2) / (double) mean1) > 0.04);
		System.out.println("  Mean insertion time: " + mean1 + "ms and " + mean2 + "ms, runs to converge:" + runs + ". " + avl);
		
		runs = 0;
		do {
			runs++;
			mean1 = insert(bst, rands);
			mean2 = insert(bst, rands);
		} while (Math.abs((mean1-mean2) / (double) mean1) > 0.04);
		System.out.println("  Mean insertion time: " + mean1 + "ms and " + mean2 + "ms, runs to converge:" + runs + ". " + bst);
		
		long start = System.currentTimeMillis();
		TreeMapRB<Integer, Integer> copy = new TreeMapRB<>(redBlack);
		long stop = System.currentTimeMillis();
		System.out.println("  Time to copy a red-black tree (sorted insertion via recursion): " +  (stop-start) +"ms. " + copy);
		
		
		//---- Test II
		System.out.println("Results for inserting integer clusters in sequences of 12 and total size: " + rands.length + " -");
		Integer [] rC = rands;
		for (int i=0; i < rC.length; i++) {
			Integer next = r.nextInt(Integer.MAX_VALUE-100);
			for (int j=next; j < next+12 && i < rC.length; j++,i++)	
				rC[i] = j;
		}
		
		runs = 0;
		do {
			runs++;
			mean1 = insert(avl, rC);
			mean2 = insert(avl, rC);
		} while (Math.abs((mean1-mean2) / (double) mean1) > 0.04);
		System.out.println("  Mean insertion time: " + mean1 + "ms and " + mean2 + "ms, runs to converge:" + runs + ". " + avl);
		
		runs = 0;
		do {
			runs++;
			mean1 = insert(redBlack, rC);
			mean2 = insert(redBlack, rC);
		} while (Math.abs((mean1-mean2) / (double) mean1) > 0.04);
		System.out.println("  Mean insertion time: " + mean1 + "ms and " + mean2 + "ms, runs to converge:" + runs + ". " + redBlack);
	}

	private static int insert(Map<Integer, Integer> tree, Integer [] rands) {
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