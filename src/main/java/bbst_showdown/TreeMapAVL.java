package bbst_showdown;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;


/**
 * An AVL tree implementation.
 * 
 * <p>
 * Because AVL trees enforce stricter balance requirements than red-black trees,
 * performance of AVL trees is better than red-black in situations where
 * red-black trees become unbalanced.
 * 
 * <p>
 * This implementation provides guaranteed log(n) time cost for the
 * {@code containsKey}, {@code get}, {@code put} and {@code remove} operations.
 * 
 * <p>
 * The starting point for this code was the red-black implementation of TreeMap from the OpenJDK:<BR>
 * http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/46c727d6ecc2/src/share/classes/java/util/TreeMap.java <BR>
 * with the <i>fixAfterInsertion</i> and <i> fixAfterDeletion</i> methods replaced.
 * 
 * @author David McManamon
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class TreeMapAVL<K, V> extends AbstractMap<K, V> {

    protected transient Entry<K, V> root = null;

    /**
     * The number of entries in the tree
     */
    protected transient int size = 0;

    /**
     * The comparator used to maintain order in this tree map, or null if it uses
     * the natural ordering of its keys.
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
     * Constructs a new, empty tree map, using the natural ordering of its keys. All
     * keys inserted into the map must implement the {@link Comparable} interface.
     * Furthermore, all such keys must be <em>mutually comparable</em>:
     * {@code k1.compareTo(k2)} must not throw a {@code ClassCastException} for any
     * keys {@code k1} and {@code k2} in the map. If the user attempts to put a key
     * into the map that violates this constraint (for example, the user attempts to
     * put a string key into a map whose keys are integers), the
     * {@code put(Object key, Object value)} call will throw a
     * {@code ClassCastException}.
     */
    public TreeMapAVL() {
	comparator = null;
    }

    /**
     * Constructs a new tree map containing the same mappings as the given map,
     * ordered according to the <em>natural ordering</em> of its keys. All keys
     * inserted into the new map must implement the {@link Comparable} interface.
     * Furthermore, all such keys must be <em>mutually comparable</em>:
     * {@code k1.compareTo(k2)} must not throw a {@code ClassCastException} for any
     * keys {@code k1} and {@code k2} in the map. This method runs in n*log(n) time.
     *
     * @param m
     *            the map whose mappings are to be placed in this map
     * @throws ClassCastException
     *             if the keys in m are not {@link Comparable}, or are not mutually
     *             comparable
     * @throws NullPointerException
     *             if the specified map is null
     */
    public TreeMapAVL(Map<? extends K, ? extends V> m) {
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
	return "AVL tree of size: " + size + ", height: " + treeHeight() + ", rotations " + rotations;
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
     * Returns the value to which the specified key is mapped, or {@code null} if
     * this map contains no mapping for the key.
     *
     * <p>
     * More formally, if this map contains a mapping from a key {@code k} to a value
     * {@code v} such that {@code key} compares equal to {@code k} according to the
     * map's ordering, then this method returns {@code v}; otherwise it returns
     * {@code null}. (There can be at most one such mapping.)
     *
     * <p>
     * A return value of {@code null} does not <em>necessarily</em> indicate that
     * the map contains no mapping for the key; it's also possible that the map
     * explicitly maps the key to {@code null}. The {@link #containsKey containsKey}
     * operation may be used to distinguish these two cases.
     *
     * @throws ClassCastException
     *             if the specified key cannot be compared with the keys currently
     *             in the map
     * @throws NullPointerException
     *             if the specified key is null and this map uses natural ordering,
     *             or its comparator does not permit null keys
     */
    public V get(Object key) {
	Entry<K, V> p = getEntry(key);
	return (p == null ? null : p.value);
    }

    /**
     * Node in the Tree. Doubles as a means to pass key-value pairs back to user
     * (see Map.Entry).
     */
    static final class Entry<K, V> implements Map.Entry<K, V> {
	K key;
	V value;
	Entry<K, V> left = null;
	Entry<K, V> right = null;
	Entry<K, V> parent = null;
	byte balance = 0; // Height(RightSubtree(N)) - Height(LeftSubtree(N)) i.e. right heavy=positive
			  // balance, left heavy negative

	/**
	 * Make a new cell with given key, value, and parent, and with {@code null}
	 * child links, and BLACK color.
	 */
	Entry(K key, V value, Entry<K, V> parent) {
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
	 * Replaces the value currently associated with the key with the given value.
	 *
	 * @return the value associated with the key before this method was called
	 */
	public V setValue(V value) {
	    V oldValue = this.value;
	    this.value = value;
	    return oldValue;
	}

	public boolean equals(Object o) {
	    if (!(o instanceof Map.Entry))
		return false;
	    Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;

	    return valEquals(key, e.getKey()) && valEquals(value, e.getValue());
	}

	public int hashCode() {
	    int keyHash = (key == null ? 0 : key.hashCode());
	    int valueHash = (value == null ? 0 : value.hashCode());
	    return keyHash ^ valueHash;
	}

	public String toString() {
	    return key + "=" + value;
	}
    }

    /**
     * Returns this map's entry for the given key, or {@code null} if the map does
     * not contain an entry for the key.
     *
     * @return this map's entry for the given key, or {@code null} if the map does
     *         not contain an entry for the key
     * @throws ClassCastException
     *             if the specified key cannot be compared with the keys currently
     *             in the map
     * @throws NullPointerException
     *             if the specified key is null and this map uses natural ordering,
     *             or its comparator does not permit null keys
     */
    final Entry<K, V> getEntry(Object key) {
	// Offload comparator-based version for sake of performance
	if (comparator != null)
	    return getEntryUsingComparator(key);
	if (key == null)
	    throw new NullPointerException();
	@SuppressWarnings("unchecked")
	Comparable<? super K> k = (Comparable<? super K>) key;
	Entry<K, V> p = root;
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
     * Version of getEntry using comparator. Split off from getEntry for
     * performance. (This is not worth doing for most methods, that are less
     * dependent on comparator performance, but is worthwhile here.)
     */
    final Entry<K, V> getEntryUsingComparator(Object key) {
	@SuppressWarnings("unchecked")
	K k = (K) key;
	Comparator<? super K> cpr = comparator;
	if (cpr != null) {
	    Entry<K, V> p = root;
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
     * Associates the specified value with the specified key in this map. If the map
     * previously contained a mapping for the key, the old value is replaced.
     *
     * @param key
     *            key with which the specified value is to be associated
     * @param value
     *            value to be associated with the specified key
     *
     * @return the previous value associated with {@code key}, or {@code null} if
     *         there was no mapping for {@code key}. (A {@code null} return can also
     *         indicate that the map previously associated {@code null} with
     *         {@code key}.)
     * @throws ClassCastException
     *             if the specified key cannot be compared with the keys currently
     *             in the map
     * @throws NullPointerException
     *             if the specified key is null and this map uses natural ordering,
     *             or its comparator does not permit null keys
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

    public void inOrderTraversal(Entry<K, V> x) {
	if (x == null)
	    return;
	inOrderTraversal(x.left);
	System.out.println(x.value + ", " + x.balance);
	inOrderTraversal(x.right);
    }

    private void fixAfterInsertion(Entry<K, V> x) {
	while (x.balance != 0) {
	    if (x.balance == 2) { // right heavy by 2?
		if (x.right.balance == 1) {
		    x.balance = 0;
		    x.right.balance = 0;
		    rotateLeft(x);
		} else { // x.right.balance = -1
		    int rlBalance = x.right.left.balance;
		    x.right.left.balance = 0;
		    x.right.balance = 0;
		    x.balance = 0;
		    if (rlBalance == 1)
			x.balance = -1;
		    else if (rlBalance == -1)
			x.right.balance = 1;

		    rotateRight(x.right);
		    rotateLeft(x);
		}
		break;
	    } else if (x.balance == -2) {
		if (x.left.balance == -1) {
		    x.balance = 0;
		    x.left.balance = 0;
		    rotateRight(x);
		} else { // x.left.balance = 1
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
		}
		break;
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


    @SuppressWarnings("unused") // didn't notice any difference in performance
    private void rotateRightLeft(Entry<K, V> p) {
	Entry<K, V> r = p.right;
	Entry<K, V> rl = p.right.left;

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

	// set z's children
	rl.right = r;
	rl.left = p;
	// set x & y's parent's
	p.parent = rl;
	r.parent = rl;
    }

    /**
     * Removes the mapping for this key from this TreeMap if present.
     *
     * @param key
     *            key for which mapping should be removed
     * @return the previous value associated with {@code key}, or {@code null} if
     *         there was no mapping for {@code key}. (A {@code null} return can also
     *         indicate that the map previously associated {@code null} with
     *         {@code key}.)
     * @throws ClassCastException
     *             if the specified key cannot be compared with the keys currently
     *             in the map
     * @throws NullPointerException
     *             if the specified key is null and this map uses natural ordering,
     *             or its comparator does not permit null keys
     */
    public V remove(Object key) {
	Entry<K, V> p = getEntry(key);
	if (p == null)
	    return null;

	V oldValue = p.value;
	deleteEntry(p);
	return oldValue;
    }

    private void deleteEntry(Entry<K, V> p) {
	modCount++;
	size--;

	// If strictly internal, copy successor's element to p and then make p point to
	// successor.
	if (p.left != null && p.right != null) {
	    Entry<K, V> s = successor(p);
	    p.key = s.key;
	    p.value = s.value;
	    p = s;
	} // p has 2 children

	// Start fixup at replacement node, if it exists.
	Entry<K, V> replacement = (p.left != null ? p.left : p.right);

	if (replacement != null) {
	    // Link replacement to parent
	    replacement.parent = p.parent;
	    if (p.parent == null) {
		root = replacement;
		return;
	    } else if (p == p.parent.left) {
		p.parent.left = replacement;
		p.parent.balance++;
		if (replacement.parent.balance == 1) {
		    p.left = p.right = p.parent = null;
		    return;
		}
	    } else {
		p.parent.right = replacement;
		p.parent.balance--;
		if (replacement.parent.balance == -1) {
		    p.left = p.right = p.parent = null;
		    return;
		}
	    }

	    // Null out links so they are OK to use by fixAfterDeletion.
	    p.left = p.right = p.parent = null;
	    fixAfterDeletion(replacement.parent);
	} else if (p.parent == null) { // return if we are the only node.
	    root = null;
	} else { // No children.
	    Entry<K, V> fixPoint = p.parent;

	    if (p == fixPoint.left) {
		fixPoint.left = null;
		fixPoint.balance++;
		p.parent = null;
		if (fixPoint.balance == 1)
		    return;
	    } else {
		fixPoint.right = null;
		fixPoint.balance--;
		p.parent = null;
		if (fixPoint.balance == -1) 
		    return;
	    }
	    
	    fixAfterDeletion(fixPoint);
	}
    }

    private void fixAfterDeletion(Entry<K, V> x) {
	while (true) {
	    if (x.balance == 2) { // right heavy by 2?
		if (x.right.balance == 1) {
		    x.balance = 0;
		    x.right.balance = 0;
		    rotateLeft(x);
		} else if (x.right.balance == 0) {
		    x.balance = 1;
		    x.right.balance = -1;
		    rotateLeft(x);
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
		    rotateRight(x.right);
		    rotateLeft(x);
		}
		x = x.parent;
	    } else if (x.balance == -2) {
		if (x.left.balance == -1) {
		    x.balance = 0;
		    x.left.balance = 0;
		    rotateRight(x);
		} else if (x.left.balance == 0) {
		    x.balance = -1;
		    x.left.balance = 1;
		    rotateRight(x);
		    break;
		} else { // (x.left.balance == 1)
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
		}
		x = x.parent;
	    } 

	    if (x.parent == null)
		break;
	    if (x.parent.left == x) {
		x.parent.balance++;
		if (x.parent.balance == 1) {
		    break;
		}
	    } else {
		x.parent.balance--;
		if (x.parent.balance == -1) {
		    break;
		}
	    }

	    x = x.parent;
	}
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map.
     *
     * <p>
     * The set's iterator returns the entries in ascending key order. The sets's
     * spliterator is <em><a href="Spliterator.html#binding">late-binding</a></em>,
     * <em>fail-fast</em>, and additionally reports {@link Spliterator#SORTED} and
     * {@link Spliterator#ORDERED} with an encounter order that is ascending key
     * order.
     *
     * <p>
     * The set is backed by the map, so changes to the map are reflected in the set,
     * and vice-versa. If the map is modified while an iteration over the set is in
     * progress (except through the iterator's own {@code remove} operation, or
     * through the {@code setValue} operation on a map entry returned by the
     * iterator) the results of the iteration are undefined. The set supports
     * element removal, which removes the corresponding mapping from the map, via
     * the {@code Iterator.remove}, {@code Set.remove}, {@code removeAll},
     * {@code retainAll} and {@code clear} operations. It does not support the
     * {@code add} or {@code addAll} operations.
     */
    public Set<Map.Entry<K, V>> entrySet() {
	EntrySet es = entrySet;
	return (es != null) ? es : (entrySet = new EntrySet());
    }

    private transient EntrySet entrySet = null;

    class EntrySet extends AbstractSet<Map.Entry<K, V>> {
	public Iterator<Map.Entry<K, V>> iterator() {
	    return new EntryIterator(getFirstEntry());
	}

	public boolean contains(Object o) {
	    if (!(o instanceof Map.Entry))
		return false;
	    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
	    Object value = entry.getValue();
	    Entry<K, V> p = getEntry(entry.getKey());
	    return p != null && valEquals(p.getValue(), value);
	}

	public boolean remove(Object o) {
	    if (!(o instanceof Map.Entry))
		return false;
	    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
	    Object value = entry.getValue();
	    Entry<K, V> p = getEntry(entry.getKey());
	    if (p != null && valEquals(p.getValue(), value)) {
		deleteEntry(p);
		return true;
	    }
	    return false;
	}

	public int size() {
	    return TreeMapAVL.this.size();
	}

	public void clear() {
	    TreeMapAVL.this.clear();
	}

	public Spliterator<Map.Entry<K, V>> spliterator() {
	    return null;
	}
    }

    /**
     * Base class for TreeMap Iterators
     */
    abstract class PrivateEntryIterator<T> implements Iterator<T> {
	Entry<K, V> next;
	Entry<K, V> lastReturned;
	int expectedModCount;

	PrivateEntryIterator(Entry<K, V> first) {
	    expectedModCount = modCount;
	    lastReturned = null;
	    next = first;
	}

	public final boolean hasNext() {
	    return next != null;
	}

	final Entry<K, V> nextEntry() {
	    Entry<K, V> e = next;
	    if (e == null)
		throw new NoSuchElementException();
	    if (modCount != expectedModCount)
		throw new ConcurrentModificationException();
	    next = successor(e);
	    lastReturned = e;
	    return e;
	}

	final Entry<K, V> prevEntry() {
	    Entry<K, V> e = next;
	    if (e == null)
		throw new NoSuchElementException();
	    if (modCount != expectedModCount)
		throw new ConcurrentModificationException();
	    next = predecessor(e);
	    lastReturned = e;
	    return e;
	}

	public void remove() {
	    if (lastReturned == null)
		throw new IllegalStateException();
	    if (modCount != expectedModCount)
		throw new ConcurrentModificationException();
	    // deleted entries are replaced by their successors
	    if (lastReturned.left != null && lastReturned.right != null)
		next = lastReturned;
	    deleteEntry(lastReturned);
	    expectedModCount = modCount;
	    lastReturned = null;
	}
    }

    final class EntryIterator extends PrivateEntryIterator<Map.Entry<K, V>> {
	EntryIterator(Entry<K, V> first) {
	    super(first);
	}

	public Map.Entry<K, V> next() {
	    return nextEntry();
	}
    }

    /**
     * Removes all of the mappings from this map. The map will be empty after this
     * call returns.
     */
    public void clear() {
	modCount++;
	size = 0;
	root = null;
	rotations = 0;
    }

    /**
     * Test two values for equality. Differs from o1.equals(o2) only in that it
     * copes with {@code null} o1 properly.
     */
    static final boolean valEquals(Object o1, Object o2) {
	return (o1 == null ? o2 == null : o1.equals(o2));
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
    static <K> K key(Entry<K, ?> e) {
	if (e == null)
	    throw new NoSuchElementException();
	return e.key;
    }

    /**
     * Returns the first Entry in the TreeMap (according to the TreeMap's key-sort
     * function). Returns null if the TreeMap is empty.
     */
    final Entry<K, V> getFirstEntry() {
	Entry<K, V> p = root;
	if (p != null)
	    while (p.left != null)
		p = p.left;
	return p;
    }

    /**
     * Returns the last Entry in the TreeMap (according to the TreeMap's key-sort
     * function). Returns null if the TreeMap is empty.
     */
    final Entry<K, V> getLastEntry() {
	Entry<K, V> p = root;
	if (p != null)
	    while (p.right != null)
		p = p.right;
	return p;
    }

    /**
     * Returns the successor of the specified Entry, or null if no such.
     */
    static <K, V> Entry<K, V> successor(Entry<K, V> t) {
	if (t == null)
	    return null;
	else if (t.right != null) {
	    Entry<K, V> p = t.right;
	    while (p.left != null)
		p = p.left;
	    return p;
	} else {
	    Entry<K, V> p = t.parent;
	    Entry<K, V> ch = t;
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
    static <K, V> Entry<K, V> predecessor(Entry<K, V> t) {
	if (t == null)
	    return null;
	else if (t.left != null) {
	    Entry<K, V> p = t.left;
	    while (p.right != null)
		p = p.right;
	    return p;
	} else {
	    Entry<K, V> p = t.parent;
	    Entry<K, V> ch = t;
	    while (p != null && ch == p.left) {
		ch = p;
		p = p.parent;
	    }
	    return p;
	}
    }

    /**
     * Return SimpleImmutableEntry for entry, or null if null
     */
    static <K, V> Map.Entry<K, V> exportEntry(TreeMapAVL.Entry<K, V> e) {
	return (e == null) ? null : new AbstractMap.SimpleImmutableEntry<>(e);
    }

    /**
     * Return key for entry, or null if null
     */
    static <K, V> K keyOrNull(TreeMapAVL.Entry<K, V> e) {
	return (e == null) ? null : e.key;
    }

    // NavigableMap API methods

    /**
     * @since 1.6
     */
    public Map.Entry<K, V> firstEntry() {
	return exportEntry(getFirstEntry());
    }

    /**
     * @since 1.6
     */
    public Map.Entry<K, V> lastEntry() {
	return exportEntry(getLastEntry());
    }

    /**
     * @since 1.6
     */
    public Map.Entry<K, V> pollFirstEntry() {
	Entry<K, V> p = getFirstEntry();
	Map.Entry<K, V> result = exportEntry(p);
	if (p != null)
	    deleteEntry(p);
	return result;
    }

    /**
     * @since 1.6
     */
    public Map.Entry<K, V> pollLastEntry() {
	Entry<K, V> p = getLastEntry();
	Map.Entry<K, V> result = exportEntry(p);
	if (p != null)
	    deleteEntry(p);
	return result;
    }

    /**
     * Gets the entry corresponding to the specified key; if no such entry exists,
     * returns the entry for the least key greater than the specified key; if no
     * such entry exists (i.e., the greatest key in the Tree is less than the
     * specified key), returns {@code null}.
     */
    final Entry<K, V> getCeilingEntry(K key) {
	Entry<K, V> p = root;
	while (p != null) {
	    int cmp = compare(key, p.key);
	    if (cmp < 0) {
		if (p.left != null)
		    p = p.left;
		else
		    return p;
	    } else if (cmp > 0) {
		if (p.right != null) {
		    p = p.right;
		} else {
		    Entry<K, V> parent = p.parent;
		    Entry<K, V> ch = p;
		    while (parent != null && ch == parent.right) {
			ch = parent;
			parent = parent.parent;
		    }
		    return parent;
		}
	    } else
		return p;
	}
	return null;
    }

    /**
     * Gets the entry corresponding to the specified key; if no such entry exists,
     * returns the entry for the greatest key less than the specified key; if no
     * such entry exists, returns {@code null}.
     */
    final Entry<K, V> getFloorEntry(K key) {
	Entry<K, V> p = root;
	while (p != null) {
	    int cmp = compare(key, p.key);
	    if (cmp > 0) {
		if (p.right != null)
		    p = p.right;
		else
		    return p;
	    } else if (cmp < 0) {
		if (p.left != null) {
		    p = p.left;
		} else {
		    Entry<K, V> parent = p.parent;
		    Entry<K, V> ch = p;
		    while (parent != null && ch == parent.left) {
			ch = parent;
			parent = parent.parent;
		    }
		    return parent;
		}
	    } else
		return p;

	}
	return null;
    }

    /**
     * Gets the entry for the least key greater than the specified key; if no such
     * entry exists, returns the entry for the least key greater than the specified
     * key; if no such entry exists returns {@code null}.
     */
    final Entry<K, V> getHigherEntry(K key) {
	Entry<K, V> p = root;
	while (p != null) {
	    int cmp = compare(key, p.key);
	    if (cmp < 0) {
		if (p.left != null)
		    p = p.left;
		else
		    return p;
	    } else {
		if (p.right != null) {
		    p = p.right;
		} else {
		    Entry<K, V> parent = p.parent;
		    Entry<K, V> ch = p;
		    while (parent != null && ch == parent.right) {
			ch = parent;
			parent = parent.parent;
		    }
		    return parent;
		}
	    }
	}
	return null;
    }

    /**
     * Returns the entry for the greatest key less than the specified key; if no
     * such entry exists (i.e., the least key in the Tree is greater than the
     * specified key), returns {@code null}.
     */
    final Entry<K, V> getLowerEntry(K key) {
	Entry<K, V> p = root;
	while (p != null) {
	    int cmp = compare(key, p.key);
	    if (cmp > 0) {
		if (p.right != null)
		    p = p.right;
		else
		    return p;
	    } else {
		if (p.left != null) {
		    p = p.left;
		} else {
		    Entry<K, V> parent = p.parent;
		    Entry<K, V> ch = p;
		    while (parent != null && ch == parent.left) {
			ch = parent;
			parent = parent.parent;
		    }
		    return parent;
		}
	    }
	}
	return null;
    }

    /**
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException
     *             if the specified key is null and this map uses natural ordering,
     *             or its comparator does not permit null keys
     * @since 1.6
     */
    public Map.Entry<K, V> lowerEntry(K key) {
	return exportEntry(getLowerEntry(key));
    }

    /**
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException
     *             if the specified key is null and this map uses natural ordering,
     *             or its comparator does not permit null keys
     * @since 1.6
     */
    public K lowerKey(K key) {
	return keyOrNull(getLowerEntry(key));
    }

    /**
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException
     *             if the specified key is null and this map uses natural ordering,
     *             or its comparator does not permit null keys
     * @since 1.6
     */
    public Map.Entry<K, V> floorEntry(K key) {
	return exportEntry(getFloorEntry(key));
    }

    /**
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException
     *             if the specified key is null and this map uses natural ordering,
     *             or its comparator does not permit null keys
     * @since 1.6
     */
    public K floorKey(K key) {
	return keyOrNull(getFloorEntry(key));
    }

    /**
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException
     *             if the specified key is null and this map uses natural ordering,
     *             or its comparator does not permit null keys
     * @since 1.6
     */
    public Map.Entry<K, V> ceilingEntry(K key) {
	return exportEntry(getCeilingEntry(key));
    }

    /**
     * @throws ClassCastException
     *             {@inheritDoc}
     * @throws NullPointerException
     *             if the specified key is null and this map uses natural ordering,
     *             or its comparator does not permit null keys
     * @since 1.6
     */
    public K ceilingKey(K key) {
	return keyOrNull(getCeilingEntry(key));
    }

    /**
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException
     *             if the specified key is null and this map uses natural ordering,
     *             or its comparator does not permit null keys
     * @since 1.6
     */
    public Map.Entry<K, V> higherEntry(K key) {
	return exportEntry(getHigherEntry(key));
    }

    /**
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException
     *             if the specified key is null and this map uses natural ordering,
     *             or its comparator does not permit null keys
     * @since 1.6
     */
    public K higherKey(K key) {
	return keyOrNull(getHigherEntry(key));
    }
}