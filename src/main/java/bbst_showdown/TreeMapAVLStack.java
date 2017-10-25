package bbst_showdown;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Set;


/**
 * An AVL tree {@link NavigableMap} implementation without parent pointers, 
 * balance factors & non-recursive insert and delete.  Re-tracing after update
 * operations is done via a stack.  This version is incomplete.
 * 
 * @author David McManamon
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class TreeMapAVLStack<K,V> extends AbstractMap<K,V> {

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
    public TreeMapAVLStack() {
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
    public TreeMapAVLStack(Map<? extends K, ? extends V> m) {
        comparator = null;
        putAll(m);
    }
    
    public int treeHeight() {
    		return treeHeight(root);
    }
    
    public int treeHeight(Entry<K, V> node) {
	if (node == null)
	    return 0;
	return (1 + Math.max(treeHeight(node.left), treeHeight(node.right)));
    }

    public int rotations() {
	return rotations;
    }
    
    public String toString() {
	return "AVL tree (no parent ref., no recursion) of size: " + size + ", height: " + treeHeight() + ", rotations "
		+ rotations;
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
        byte balance = 0;  // Height(RightSubtree(N)) - Height(LeftSubtree(N)) i.e. right heavy=positive balance, left heavy negative

        /**
         * Make a new cell with given key, value, and parent, and with
         * {@code null} child links, and BLACK color.
         */
        Entry(K key, V value, Entry<K,V> parent) {
            this.key = key;
            this.value = value;
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
    // TODO
    private byte[] direction = new byte[100];
    @SuppressWarnings("unchecked")
    private Entry<K,V> [] stack = new Entry[100];
    
    public V put(K key, V value) {
    	//System.out.println("Inserting:" + key);
        Entry<K,V> t = root;
        if (t == null) {
            compare(key, key); // type (and possibly null) check

            root = new Entry<>(key, value, null);
            size = 1;
            modCount++;
            return null;
        }
        int cmp;
        Entry<K,V> parent;
        int treeDepth = 0;
        // split comparator and comparable paths
        Comparator<? super K> cpr = comparator;
        if (cpr != null) {
            do {
                parent = t;
                cmp = cpr.compare(key, t.key);
                if (cmp < 0) {
                    t = t.left;
                    // TODO
                } else if (cmp > 0) {
                    t = t.right;
                }else {
                    return t.setValue(value);
                }
            } while (t != null);
        } else {
            if (key == null)
                throw new NullPointerException();
            @SuppressWarnings("unchecked")
                Comparable<? super K> k = (Comparable<? super K>) key;
            do {
                parent = t;
                cmp = k.compareTo(t.key);
                if (cmp < 0) {
                		stack[treeDepth] = parent;
                		direction[treeDepth++] = -1;
                    t = t.left;
                } else if (cmp > 0) {
                		stack[treeDepth] = parent;
                		direction[treeDepth++] = +1;
                    t = t.right;
                } else {
                    return t.setValue(value);
                }
            } while (t != null);
        }
        
        Entry<K,V> e = new Entry<>(key, value, parent);
        if (cmp < 0) {
            parent.left = e;
        } else {
            parent.right = e;
        }
        fixAfterInsertion(parent, --treeDepth);
        
        size++;
        modCount++;
        return null;
    }
    
    private void fixAfterInsertion(Entry<K, V> x, int treeDepth) {
	while ((x.balance += direction[treeDepth]) != 0) {
	    if (x.balance == 2) { // right heavy by 2?
		if (x.right.balance == 1) {
		    x.balance = 0;
		    x.right.balance = 0;
		    x = rotateLeft(x);
		    break;
		} else { // x.right.balance = -1
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
		    break;
		}
	    } else if (x.balance == -2) {
		if (x.left.balance == -1) {
		    x.balance = 0;
		    x.left.balance = 0;
		    x = rotateRight(x);
		    break;
		} else { // x.left.balance = 1
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
		    break;
		}
	    }
	    treeDepth--;
	    if (treeDepth == -1) {
		return;
	    }
	    x = stack[treeDepth];
	}
	// set parent reference if necessary
	treeDepth--;

	if (treeDepth == -1)
	    root = x;
	else if (direction[treeDepth] == 1)
	    stack[treeDepth].right = x;
	else
	    stack[treeDepth].left = x;
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

    public void inOrderTraversal(Entry<K, V> x) {
	if (x == null)
	    return;
	inOrderTraversal(x.left);
	System.out.println(x.value + ", " + x.balance);
	inOrderTraversal(x.right);
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
        Entry<K,V> p = getEntry(key);
        if (p == null)
            return null;

        V oldValue = p.value;
        deleteEntry(p);
        return oldValue;
    }

    private void deleteEntry(Entry<K, V> p) {
	modCount++;
	size--;
	// TODO
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
     * @throws NoSuchElementException if the Entry is null
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
        	// TODO
        	/*
            Entry<K,V> p = t.parent;
            Entry<K,V> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
            */
        	return null;	
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
        	// TODO
        	/*
            Entry<K,V> p = t.parent;
            Entry<K,V> ch = t;
            while (p != null && ch == p.left) {
                ch = p;
                p = p.parent;
            }
            return p;
            */
        	return null;
        }
    }

    /**
     * Return SimpleImmutableEntry for entry, or null if null
     */
    static <K,V> Map.Entry<K,V> exportEntry(TreeMapAVLStack.Entry<K,V> e) {
        return (e == null) ? null :
            new AbstractMap.SimpleImmutableEntry<>(e);
    }

    /**
     * Return key for entry, or null if null
     */
    static <K,V> K keyOrNull(TreeMapAVLStack.Entry<K,V> e) {
        return (e == null) ? null : e.key;
    }
    
    // NavigableMap API methods

    /**
     * @since 1.6
     */
    public Map.Entry<K,V> firstEntry() {
        return exportEntry(getFirstEntry());
    }

    /**
     * @since 1.6
     */
    public Map.Entry<K,V> lastEntry() {
        return exportEntry(getLastEntry());
    }

    /**
     * @since 1.6
     */
    public Map.Entry<K,V> pollFirstEntry() {
        Entry<K,V> p = getFirstEntry();
        Map.Entry<K,V> result = exportEntry(p);
        if (p != null)
            deleteEntry(p);
        return result;
    }

    /**
     * @since 1.6
     */
    public Map.Entry<K,V> pollLastEntry() {
        Entry<K,V> p = getLastEntry();
        Map.Entry<K,V> result = exportEntry(p);
        if (p != null)
            deleteEntry(p);
        return result;
    }

    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
	// TODO Auto-generated method stub
	return null;
    }
}