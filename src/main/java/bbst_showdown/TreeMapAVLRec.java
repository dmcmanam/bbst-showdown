package bbst_showdown;

import java.util.Comparator;

/**
 * There are many elegant online AVL implementations that use recursion and height. <BR>
 * This AVL insert implementation uses balance factor instead of height which adds some
 * complexity compared to the most common recursive implementation.
 * 
 * @author David McManamon
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class TreeMapAVLRec<K, V> extends AVLTreeMap<K, V> {

    public TreeMapAVLRec() {
	super();
    }
	
    public V put(K key, V value) {
        Entry<K,V> t = root;
        if (t == null) {
            compare(key, key); // type (and possibly null) check

            root = new Entry<K,V>(key, value, null);
            size = 1;
            modCount++;
            return null;
        }
        int cmp;
        // split comparator and comparable paths
        Comparator<? super K> cpr = comparator;
        if (cpr != null) {
            do {
                cmp = cpr.compare(key, t.key);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else
                    return t.setValue(value);
            } while (t != null);
        } else {
            if (key == null) throw new NullPointerException();
            rebalanced = false;
            root = put(root, key, value);
        }
        
        size++;
        modCount++;
        return null;
    }
    
    boolean rebalanced;  // flag to signal re-tracing can stop after insertion because a rebalance was performed or isn't necessary
    
    private Entry<K, V> put(Entry<K, V> x, K key, V value) {
	if (x == null)
	    return new Entry<>(key, value, null);
	@SuppressWarnings("unchecked")
	Comparable<? super K> k = (Comparable<? super K>) key;
	int cmp = k.compareTo(x.key);
	if (cmp < 0) {
	    x.left = put(x.left, key, value);
	    if (!rebalanced)
		x.balance--;
	} else if (cmp > 0) {
	    x.right = put(x.right, key, value);
	    if (!rebalanced)
		x.balance++;
	} else {
	    x.value = value;
	    rebalanced = true;
	    size--;
	    return x;
	}

	if (x.balance == 0) {
	    rebalanced = true;
	} else if (Math.abs(x.balance) == 2) {
	    rebalanced = true;
	    return balance(x);
	}
	return x;
    }

    /**
     * Restores the AVL tree property of the subtree.
     * 
     * @param x the subtree
     * @return the subtree with restored AVL property
     */
    private Entry<K, V> balance(Entry<K, V> x) {
	if (x.balance == -2) {
	    if (x.left.balance == 1) {
		int lrBalance = x.left.right.balance;
		x.left.right.balance = 0;
		x.left.balance = 0;
		x.balance = 0;
		if (lrBalance == 1)
		    x.left.balance = -1;
		else if (lrBalance == -1)
		    x.balance = 1;
		x.left = rotateLeft(x.left);
		x = rotateRight(x);
	    } else {
		x.balance = 0;
		x.left.balance = 0;
		x = rotateRight(x);
	    }
	} else if (x.balance == 2) {
	    if (x.right.balance == -1) {
		int rlBalance = x.right.left.balance;
		x.right.left.balance = 0;
		x.right.balance = 0;
		x.balance = 0;
		if (rlBalance == 1)
		    x.balance = -1;
		else if (rlBalance == -1)
		    x.right.balance = 1;
		x.right = rotateRight(x.right);
		x = rotateLeft(x);
	    } else {
		x.balance = 0;
		x.right.balance = 0;
		x = rotateLeft(x);
	    }
	}
	return x;
    }

    private Entry<K, V> rotateLeft(Entry<K, V> p) {
	Entry<K, V> r = p.right;
	p.right = r.left;
	r.left = p;
	rotations++;
	return r;
    }

    private Entry<K, V> rotateRight(Entry<K, V> p) {
	Entry<K, V> l = p.left;
	p.left = l.right;
	l.right = p;
	rotations++;
	return l;
    }

    public String toString() {
	return "AVL(recursive w/balance factor) tree of size: " + size + ", height: " + treeHeight() + ", rotations " + rotations;
    }
}