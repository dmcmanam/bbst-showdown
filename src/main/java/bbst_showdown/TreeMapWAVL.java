package bbst_showdown;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;


/**
 * A WAVL tree implementation with rank and no recursion.
 * 
 * https://en.wikipedia.org/wiki/WAVL_tree
 * 
 * <p>This implementation provides guaranteed log(n) time cost for the
 * {@code containsKey}, {@code get}, {@code put} and {@code remove}
 * operations. 
 * 
 * @author David McManamon
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class TreeMapWAVL<K, V> extends AbstractMap<K, V> {
    
    protected transient Entry<K, V> root = null;

    /**
     * The number of entries in the tree
     */
    protected transient int size = 0;
    
    /**
     * The comparator used to maintain order in this tree map, or
     * null if it uses the natural ordering of its keys.
     *
     * @serial
     */
    protected final Comparator<? super K> comparator;
    
    /**
     * The number of structural modifications to the tree.
     */
    protected transient int modCount = 0;
    
    protected transient int rotations = 0;
    
    /**
     * Constructs a new, empty tree map, using the natural ordering of its
     * keys.  All keys inserted into the map must implement the {@link
     * Comparable} interface.  Furthermore, all such keys must be
     * <em>mutually comparable</em>: {@code k1.compareTo(k2)} must not throw
     * a {@code ClassCastException} for any keys {@code k1} and
     * {@code k2} in the map.  If the user attempts to put a key into the
     * map that violates this constraint (for example, the user attempts to
     * put a string key into a map whose keys are integers), the
     * {@code put(Object key, Object value)} call will throw a
     * {@code ClassCastException}.
     */
    public TreeMapWAVL() {
	comparator = null;
    }
    
    /**
     * Constructs a new tree map containing the same mappings as the given
     * map, ordered according to the <em>natural ordering</em> of its keys.
     * All keys inserted into the new map must implement the {@link
     * Comparable} interface.  Furthermore, all such keys must be
     * <em>mutually comparable</em>: {@code k1.compareTo(k2)} must not throw
     * a {@code ClassCastException} for any keys {@code k1} and
     * {@code k2} in the map.  This method runs in n*log(n) time.
     *
     * @param  m the map whose mappings are to be placed in this map
     * @throws ClassCastException if the keys in m are not {@link Comparable},
     *         or are not mutually comparable
     * @throws NullPointerException if the specified map is null
     */
    public TreeMapWAVL(Map<? extends K, ? extends V> m) {
        comparator = null;
        putAll(m);
    }
    
    public int treeHeight() {
	return treeHeight(root) - 1;
    }

    protected int treeHeight(Entry<K, V> node) {
	if (node == null)
	    return 0;
	return (1 + Math.max(treeHeight(node.left), treeHeight(node.right)));
    }
    
    public int rotations() {
	return rotations;
    }

    public String toString() {
	return "WAVL tree of size: " + size + ", height: " + treeHeight() + ", rotations " + rotations;
    }
    
    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    public int size() {
        return size;
    }
    
    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code key} compares
     * equal to {@code k} according to the map's ordering, then this
     * method returns {@code v}; otherwise it returns {@code null}.
     * (There can be at most one such mapping.)
     *
     * <p>A return value of {@code null} does not <em>necessarily</em>
     * indicate that the map contains no mapping for the key; it's also
     * possible that the map explicitly maps the key to {@code null}.
     * The {@link #containsKey containsKey} operation may be used to
     * distinguish these two cases.
     *
     * @throws ClassCastException if the specified key cannot be compared
     *         with the keys currently in the map
     * @throws NullPointerException if the specified key is null
     *         and this map uses natural ordering, or its comparator
     *         does not permit null keys
     */
    public V get(Object key) {
        Entry<K,V> p = getEntry(key);
        return (p==null ? null : p.value);
    }
	
	/**
     * Node in the Tree.  
     * Doubles as a means to pass key-value pairs back to
     * user (see Map.Entry).
     */
    static final class Entry<K,V> implements Map.Entry<K,V> {
        K key;
        V value;
        Entry<K,V> left = null;
        Entry<K,V> right = null;
        Entry<K,V> parent = null;
        byte rank = 0;

        /**
         * Make a new cell with given key, value, and parent, and with
         * {@code null} child links, and BLACK color.
         */
        Entry(K key, V value, Entry<K,V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        /**
         * Returns the key.
         *
         * @return the key
         */
        public K getKey() {
            return key;
        }

        /**
         * Returns the value associated with the key.
         *
         * @return the value associated with the key
         */
        public V getValue() {
            return value;
        }

        /**
         * Replaces the value currently associated with the key with the given
         * value.
         *
         * @return the value associated with the key before this method was
         *         called
         */
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<?,?> e = (Map.Entry<?,?>)o;

            return valEquals(key,e.getKey()) && valEquals(value,e.getValue());
        }

        public int hashCode() {
            int keyHash = (key==null ? 0 : key.hashCode());
            int valueHash = (value==null ? 0 : value.hashCode());
            return keyHash ^ valueHash;
        }

        public String toString() {
            return key + "=" + value;
        }
    }
    
    /**
     * Returns this map's entry for the given key, or {@code null} if the map
     * does not contain an entry for the key.
     *
     * @return this map's entry for the given key, or {@code null} if the map
     *         does not contain an entry for the key
     * @throws ClassCastException if the specified key cannot be compared
     *         with the keys currently in the map
     * @throws NullPointerException if the specified key is null
     *         and this map uses natural ordering, or its comparator
     *         does not permit null keys
     */
    final Entry<K,V> getEntry(Object key) {
    		// Offload comparator-based version for sake of performance
        if (comparator != null)
            return getEntryUsingComparator(key);
        if (key == null)
            throw new NullPointerException();
        @SuppressWarnings("unchecked")
            Comparable<? super K> k = (Comparable<? super K>) key;
        Entry<K,V> p = root;
        while (p != null) {
            int cmp = k.compareTo(p.key);
            if (cmp < 0)
                p = p.left;
            else if (cmp > 0)
                p = p.right;
            else
                return p;
        }
        return null;
    }
    
    /**
     * Version of getEntry using comparator. Split off from getEntry
     * for performance. (This is not worth doing for most methods,
     * that are less dependent on comparator performance, but is
     * worthwhile here.)
     */
    final Entry<K,V> getEntryUsingComparator(Object key) {
        @SuppressWarnings("unchecked")
            K k = (K) key;
        Comparator<? super K> cpr = comparator;
        if (cpr != null) {
            Entry<K,V> p = root;
            while (p != null) {
                int cmp = cpr.compare(k, p.key);
                if (cmp < 0)
                    p = p.left;
                else if (cmp > 0)
                    p = p.right;
                else
                    return p;
            }
        }
        return null;
    }
    
    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old
     * value is replaced.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     *
     * @return the previous value associated with {@code key}, or
     *         {@code null} if there was no mapping for {@code key}.
     *         (A {@code null} return can also indicate that the map
     *         previously associated {@code null} with {@code key}.)
     * @throws ClassCastException if the specified key cannot be compared
     *         with the keys currently in the map
     * @throws NullPointerException if the specified key is null
     *         and this map uses natural ordering, or its comparator
     *         does not permit null keys
     */
    public V put(K key, V value) {
	Entry<K, V> t = root;
	if (t == null) {
	    compare(key, key); // type (and possibly null) check

	    root = new Entry<>(key, value, null);
	    size = 1;
	    modCount++;
	    return null;
	}
	int cmp;
	Entry<K, V> parent;
	// split comparator and comparable paths
	Comparator<? super K> cpr = comparator;
	if (cpr != null) {
	    do {
		parent = t;
		cmp = cpr.compare(key, t.key);
		if (cmp < 0)
		    t = t.left;
		else if (cmp > 0)
		    t = t.right;
		else
		    return t.setValue(value);
	    } while (t != null);
	} else {
	    if (key == null)
		throw new NullPointerException();
	    @SuppressWarnings("unchecked")
	    Comparable<? super K> k = (Comparable<? super K>) key;
	    do {
		parent = t;
		cmp = k.compareTo(t.key);
		if (cmp < 0)
		    t = t.left;
		else if (cmp > 0)
		    t = t.right;
		else
		    return t.setValue(value);
	    } while (t != null);
	}

	Entry<K, V> e = new Entry<>(key, value, parent);
	if (cmp < 0) {
	    parent.left = e;
	} else {
	    parent.right = e;
	}

	if (parent.rank == 0) {
	    fixAfterInsertion(parent);
	}

	size++;
	modCount++;
	return null;
    }
    
    public void inOrderTraversal(Entry<K, V> x) {
	if (x == null)
	    return;
	inOrderTraversal(x.left);
	System.out.println(x.value + ", " + x.rank);
	inOrderTraversal(x.right);
    }
    
    /**
If the path of incremented ranks reaches the root of the tree, then the rebalancing procedure stops.
If the path of incremented ranks reaches a node whose parent's rank previously differed by two and after incrementing now differ by one,
the rebalancing procedure stops without changing the structure of the tree.
If the procedure increases the rank of a node x, so that it becomes equal to the rank of the parent y of x, 
  but the other child of y has a rank that is smaller by two (so that the rank of y cannot be increased) 
  then again the rebalancing procedure stops after performing rotations necessary.
     */
    private void fixAfterInsertion(Entry<K, V> x) {
	x.rank++;
	while (x.parent != null && x.rank + 1 != x.parent.rank) {
	    Entry<K, V> p = x.parent;
	    if (p.left == x) { // parent's left node = this so check the left side
		if (needToRotateLeftHeavy(p)) {
		    if (x.right != null && (x.rank - x.right.rank) == 1) {
			x.rank--;
			x.right.rank++;
			rotateLeft(x);
		    }
		    p.rank--;
		    rotateRight(p);
		    break;
		}
	    } else {
		if (needToRotateRightHeavy(p)) {
		    if (x.left != null && (x.rank - x.left.rank) == 1) {
			x.rank--;
			x.left.rank++;
			rotateRight(x);
		    }
		    p.rank--;
		    rotateLeft(p);
		    break;
		}
	    }

	    x = x.parent;
	    x.rank++;
	}
    }

    private boolean needToRotateRightHeavy(Entry<K, V> p) {
	if (p.left == null) {
	    if (p.rank == 1)
		return true;
	    return false;
	} else if (p.left.rank + 2 == p.rank)
	    return true;
	return false;
    }

    private boolean needToRotateLeftHeavy(Entry<K, V> p) {
	if (p.right == null) {
	    if (p.rank == 1)
		return true;
	    return false;
	} else if (p.rank - p.right.rank == 2)
	    return true;
	return false;
    }
    
    /** From CLR */
    private void rotateLeft(Entry<K, V> p) {
	Entry<K, V> r = p.right;
	p.right = r.left;
	if (r.left != null)
	    r.left.parent = p;
	r.parent = p.parent;
	if (p.parent == null)
	    root = r;
	else if (p.parent.left == p)
	    p.parent.left = r;
	else
	    p.parent.right = r;
	r.left = p;
	p.parent = r;
	rotations++;
    }

    /** From CLR */
    private void rotateRight(Entry<K, V> p) {
	Entry<K, V> l = p.left;
	p.left = l.right;
	if (l.right != null)
	    l.right.parent = p;
	l.parent = p.parent;
	if (p.parent == null)
	    root = l;
	else if (p.parent.right == p)
	    p.parent.right = l;
	else
	    p.parent.left = l;
	l.right = p;
	p.parent = l;
	rotations++;
    }

    /**
     * Removes the mapping for this key from this TreeMap if present.
     *
     * @param  key key for which mapping should be removed
     * @return the previous value associated with {@code key}, or
     *         {@code null} if there was no mapping for {@code key}.
     *         (A {@code null} return can also indicate that the map
     *         previously associated {@code null} with {@code key}.)
     * @throws ClassCastException if the specified key cannot be compared
     *         with the keys currently in the map
     * @throws NullPointerException if the specified key is null
     *         and this map uses natural ordering, or its comparator
     *         does not permit null keys
     */
    public V remove(Object key) {
	Entry<K, V> p = getEntry(key);
	if (p == null)
	    return null;

	V oldValue = p.value;
	deleteEntry(p);
	return oldValue;
    }
    
    /**
     * Delete node p, and then rebalance the tree.
     */
    private void deleteEntry(Entry<K,V> p) {
        modCount++;
        size--;

        // If strictly internal, copy successor's element to p and then make p
        // point to successor.
        if (p.left != null && p.right != null) {
            Entry<K,V> s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        } // p has 2 children

        // Start fixup at replacement node, if it exists.
        Entry<K,V> replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) {
            // Link replacement to parent
	    replacement.parent = p.parent;
	    Entry<K, V> mirror = null;
	    if (p.parent == null) {
		root = replacement;
		return;
	    } else if (p == p.parent.left) {
		p.parent.left = replacement;
		mirror = p.parent.right;
	    } else {
		p.parent.right = replacement;
		mirror = p.parent.left;
	    }

	    // Null out links so they are OK to use by fixAfterDeletion.
	    p.left = p.right = p.parent = null;

	    // TODO
	    if (mirror == null || (replacement.parent.rank - mirror.rank) != 1) {
		fixAfterDeletion(replacement.parent, mirror);
	    }
        } else if (p.parent == null) { // return if we are the only node.
            root = null;
        } else { //  No children. Use self as phantom replacement and unlink.
            // TODO check if null check necessary?
            Entry<K, V> fixPoint = p.parent;
            Entry<K, V> mirror = null;

	    if (p == p.parent.left) {
		p.parent.left = null;
		mirror = fixPoint.right;
	    } else if (p == p.parent.right) {
		p.parent.right = null;
		mirror = fixPoint.left;
	    }
	    p.parent = null;

	    if (mirror == null || (fixPoint.rank - p.rank >= 2) || (fixPoint.rank - mirror.rank) != 1 ) {
		fixAfterDeletion(fixPoint, mirror);
	    }
        }
    }
    
    //mirror node is a term from Knuth's Art of CP, it refers to the parent node's other child.
    private void fixAfterDeletion(Entry<K, V> p, Entry<K,V> mirror) {
	do {
	    p.rank--;
	    if (p.left == mirror) { // check if left heavy?
		if (mirror != null && p.rank - mirror.rank <= 0) {
		    if (mirror.right != null && (mirror.rank - mirror.right.rank) == 1) {
			mirror.rank--;
			mirror.right.rank++;
			rotateLeft(mirror);
		    }
		    p.rank--;
		    rotateRight(p);
		    break;
		}
	    } else {
		if (mirror != null && p.rank - mirror.rank <= 0) {
		    if (mirror.left != null && (mirror.rank - mirror.left.rank) == 1) {
			mirror.rank--;
			mirror.left.rank++;
			rotateRight(mirror);
		    }
		    p.rank--;
		    rotateLeft(p);
		    break;
		}
	    }
	    
	    mirror = (p.parent == p.left) ? p.right : p.left;
	    p = p.parent;
	} while (p != null && (p.rank - mirror.rank) != 1);
    }
    
    /**
     * Removes all of the mappings from this map.
     * The map will be empty after this call returns.
     */
    public void clear() {
    	modCount++;
        size = 0;
        root = null;
        rotations = 0;
    }
    
    /**
     * Test two values for equality.  Differs from o1.equals(o2) only in
     * that it copes with {@code null} o1 properly.
     */
    static final boolean valEquals(Object o1, Object o2) {
        return (o1==null ? o2==null : o1.equals(o2));
    }

    /**
     * Compares two keys using the correct comparison method for this TreeMap.
     */
    @SuppressWarnings("unchecked")
    final int compare(Object k1, Object k2) {
	return comparator == null ? ((Comparable<? super K>) k1).compareTo((K) k2) : comparator.compare((K) k1, (K) k2);
    }

    /**
     * Returns the key corresponding to the specified Entry.
     * 
     * @throws NoSuchElementException
     *             if the Entry is null
     */
    static <K> K key(Entry<K,?> e) {
        if (e==null)
            throw new NoSuchElementException();
        return e.key;
    }
	
	/**
     * Returns the first Entry in the TreeMap (according to the TreeMap's
     * key-sort function).  Returns null if the TreeMap is empty.
     */
    final Entry<K,V> getFirstEntry() {
        Entry<K,V> p = root;
        if (p != null)
            while (p.left != null)
                p = p.left;
        return p;
    }

    /**
     * Returns the last Entry in the TreeMap (according to the TreeMap's
     * key-sort function).  Returns null if the TreeMap is empty.
     */
    final Entry<K,V> getLastEntry() {
        Entry<K,V> p = root;
        if (p != null)
            while (p.right != null)
                p = p.right;
        return p;
    }
    
    /**
     * Returns the successor of the specified Entry, or null if no such.
     */
    static <K,V> Entry<K,V> successor(Entry<K,V> t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            Entry<K,V> p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
            Entry<K,V> p = t.parent;
            Entry<K,V> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    /**
     * Returns the predecessor of the specified Entry, or null if no such.
     */
    static <K,V> Entry<K,V> predecessor(Entry<K,V> t) {
        if (t == null)
            return null;
        else if (t.left != null) {
            Entry<K,V> p = t.left;
            while (p.right != null)
                p = p.right;
            return p;
        } else {
            Entry<K,V> p = t.parent;
            Entry<K,V> ch = t;
            while (p != null && ch == p.left) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
	// TODO Auto-generated method stub
	return null;
    }
}