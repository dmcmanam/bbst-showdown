package performanceTests;

import java.util.Map;

import bbst_showdown.TreeMapAVL;
import bbst_showdown.TreeMapRedBlack;
import bbst_showdown.TreeMapWAVL;

public class CountRotations {
    public static void main(String [] args) {
	java.util.Random r = new java.util.Random();
	Integer[] groupedRandomNumbers = new Integer [300000];
	for (int i = 0; i < groupedRandomNumbers.length;) {
	    Integer nextRand = r.nextInt(Integer.MAX_VALUE - 16);
	    for (int j=0; j < 16; j++) {
		groupedRandomNumbers[i++] = nextRand + j;
	    }
	}
	
	System.out.println("Results for inserting integer clusters in sequences of 16 and total size: " + groupedRandomNumbers.length + " -");
	
	TreeMapWAVL<Integer, Integer> wavl = new TreeMapWAVL<>();
	TreeMapAVL<Integer, Integer> avl = new TreeMapAVL<>();
	TreeMapRedBlack<Integer, Integer> redBlack = new TreeMapRedBlack<>();
	
	insert(avl, groupedRandomNumbers);
	insert(wavl, groupedRandomNumbers);
	insert(redBlack, groupedRandomNumbers);
    }
    
    private static int insert(Map<Integer, Integer> tree, Integer[] rands) {
	long start = System.currentTimeMillis();
	for (int run = 0; run < 10; run++) {
	    tree.clear();
	    for (int i = 0; i < rands.length; i++) {
		tree.put(rands[i], rands[i]);
	    }
	}
	long stop = System.currentTimeMillis();

	int mean = (int) (stop - start) / 10;
	
	System.out.println("  Mean insertion time: " + mean + ". " + tree);
	return mean;
    }
}