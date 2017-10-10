package bbst_showdown;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class TreeMapAVL<K,V>
    extends AbstractMap<K,V>
    implements NavigableMap<K,V>, Cloneable, java.io.Serializable {

	private static final long serialVersionUID = -3345445960366808335L;

	public transient Entry<K,V> root = null;

    /**
     * The number of entries in the tree
     */
    private transient int size = 0;
    
    /**
     * The comparator used to maintain order in this tree map, or
     * null if it uses the natural ordering of its keys.
     *
     * @serial
     */
    private final Comparator<? super K> comparator = null;
    
    /**
     * The number of structural modifications to the tree.
     */
    private transient int modCount = 0;
    
    public TreeMapAVL() {
    		
    }
    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    public int size() {
        return size;
    }
    
    public V get(Object key) {
        Entry<K,V> p = getEntry(key);
        return (p==null ? null : p.value);
    }
	
	/**
     * Node in the Tree.  Doubles as a means to pass key-value pairs back to
     * user (see Map.Entry).
     */
    static final class Entry<K,V> implements Map.Entry<K,V> {
        K key;
        V value;
        Entry<K,V> left = null;
        Entry<K,V> right = null;
        Entry<K,V> parent = null;
        byte balance = 0;

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
        //if (comparator != null) TODO
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
        }
        else {
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
        
        Entry<K,V> e = new Entry<>(key, value, parent);
        if (cmp < 0) {
            parent.left = e;
            parent.balance--;
        } else {
            parent.right = e;
            parent.balance++;
        }
        fixAfterInsertion(parent);
        
        size++;
        modCount++;
        return null;
    }
    
    public void inOrderTraversal(Entry<K,V> x) {
    		if (x == null)
    			return;
    		inOrderTraversal(x.left);
    		System.out.println(x.value + ", " + x.balance);
    		inOrderTraversal(x.right);
    }
    
    private void fixAfterInsertion(Entry<K,V> x) {
    		while (x.balance != 0) {
    			if (x.balance == 2) { // right heavy by 2? 
    				if (x.right.balance == 1) {
    					x.balance = 0;
    					x.right.balance = 0;
    					rotateLeft(x);
    					break;
    				} else { // x.right.balance = -1
    					//System.out.println(x.balance + "," + x.right.balance + "," + x.right.left.balance);
    					int rlBalance = x.right.left.balance;
    					x.right.left.balance = 0;
    					x.right.balance = 0;
    					x.balance = 0;
    					if (rlBalance == 1)
    						x.balance = -1;
    					else if (rlBalance == -1)
    						x.right.balance = 1;
    					
    					//These 2 calls would produce the same result: rotateRight(x.right); rotateLeft(x);
    					rotateRightLeft(x);
    					break;
    				}
    			} else if (x.balance == -2) {
    				if (x.left.balance == -1) {
    					x.balance = 0;
    					x.left.balance = 0;
    					rotateRight(x);
    					break;
    				} else { // x.left.balance = 1
    					//System.out.println(x.left.balance);
    					int lrBalance = x.left.right.balance;
    					x.left.right.balance = 0;
    					x.left.balance = 0;
    					x.balance = 0;
    					if (lrBalance == 1)
    						x.left.balance = -1;
    					else if (lrBalance == -1)
    						x.balance = 1;
    					
    					rotateLeft(x.left);
    					rotateRight(x);
    					break;
    				}
    			}
    			
    			if (x.parent == null)
    				break;
    			if (x.parent.left == x)
    				x.parent.balance--;
    			else 
    				x.parent.balance++;
    			
    			x = x.parent;
    		}
    }

    /** From CLR */
    private void rotateLeft(Entry<K,V> p) {
    		//System.out.println("Left.");
        if (p != null) {
            Entry<K,V> r = p.right;
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
        }
    }
    
    private void rotateRightLeft(Entry<K,V> p) {
    		Entry<K,V> r = p.right;
    		Entry<K,V> rl = p.right.left;
    		
    		// set x.right & y.left
    		p.right = rl.left;
    		r.left = rl.right;
    		
    		// set b & c & z's parents
    		if (rl.left != null)
    			rl.left.parent = p;
    		if (rl.right != null)
    			rl.right.parent = r;
    		rl.parent = p.parent;
    		
    		if (p.parent == null)
    			root = rl;
    		else if (p.parent.left == p)
    			p.parent.left = rl; 
        else
        		p.parent.right = rl;
    		
    		// set x & y's & z's parents & z's children
    		rl.right = r;
    		rl.left = p;
    		p.parent = rl;
    		r.parent = rl;
    }

    /** From CLR */
	private void rotateRight(Entry<K, V> p) {
		// System.out.println("Right.");
		if (p != null) {
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
		}
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

    /**
     * Compares two keys using the correct comparison method for this TreeMap.
     */
    @SuppressWarnings("unchecked")
    final int compare(Object k1, Object k2) {
        return comparator==null ? ((Comparable<? super K>)k1).compareTo((K)k2)
            : comparator.compare((K)k1, (K)k2);
    }
    
    private void deleteEntry(Entry<K, V> p) {
		// TODO Auto-generated method stub
	}
    
	/**
     * Removes all of the mappings from this map.
     * The map will be empty after this call returns.
     */
    public void clear() {
        size = 0;
        root = null;
    }
    
    /**
     * Test two values for equality.  Differs from o1.equals(o2) only in
     * that it copes with {@code null} o1 properly.
     */
    static final boolean valEquals(Object o1, Object o2) {
        return (o1==null ? o2==null : o1.equals(o2));
    }

	@Override
	public Comparator<? super K> comparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public K firstKey() {
		return key(getFirstEntry());
	}
	
	@Override
	public K lastKey() {
		return key(getLastEntry());
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
	public java.util.Map.Entry<K, V> lowerEntry(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public K lowerKey(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public java.util.Map.Entry<K, V> floorEntry(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public K floorKey(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public java.util.Map.Entry<K, V> ceilingEntry(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public K ceilingKey(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public java.util.Map.Entry<K, V> higherEntry(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public K higherKey(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public java.util.Map.Entry<K, V> firstEntry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public java.util.Map.Entry<K, V> lastEntry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public java.util.Map.Entry<K, V> pollFirstEntry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public java.util.Map.Entry<K, V> pollLastEntry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NavigableMap<K, V> descendingMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NavigableSet<K> navigableKeySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NavigableSet<K> descendingKeySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedMap<K, V> subMap(K fromKey, K toKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedMap<K, V> headMap(K toKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedMap<K, V> tailMap(K fromKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	public static void main(String [] args) {
		// TODO convert to test platform
		Map<Integer, Integer> x = new TreeMapAVL<>();
		
		System.out.println(lookup(x) + " ms " + x.size()+ " elements..");
		
		//System.out.println(insertRandomOrder(x) + " ms to insert " + x.size()+ " elements.");
	}
	
	public static long insertRandomOrder(Map<Integer, Integer> x) {
		long start = System.currentTimeMillis();
		java.util.Random r = new java.util.Random();
		for(Integer i=0; i < 1000000; i++) {
			int next = r.nextInt();
			x.put(next, next);
		}
		long stop = System.currentTimeMillis();
		return stop - start;
	}
	
	public static long insertInOrder(Map<Integer, Integer> x) {
		long start = System.currentTimeMillis();
		for(Integer i=0; i < 1000000; i++) {
			x.put(i, i);
		}
		long stop = System.currentTimeMillis();
		return stop - start;
	}
	
	public static long lookup(Map<Integer, Integer> x) {
		
		java.util.Random r = new java.util.Random();
		Integer [] inserted = new Integer[500000];
		for(Integer i=0; i < 500000; i++) {
			Integer next = r.nextInt();
			inserted[i] = next;
			x.put(next, next);
		}
		
		long start = System.currentTimeMillis();
		
		for(Integer i=200000; i < 500000; i++) {
			Integer result = x.get(inserted[i]);
			if (result == null)
				System.err.println("Error.");
		}
		
		long stop = System.currentTimeMillis();
		return stop - start;
	}
}