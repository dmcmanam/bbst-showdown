# bbst-showdown
Performance of AVL &amp; Red-black trees compared in Java.

AVL trees will be shown to perform better than red-black trees for degenerate cases such as inserting 1 million elements in-order.

Other factors commonly assumed to make red-black or AVL trees superior do not impact performance as much as commonly assumed.  For example, red-black trees perform .6 rotations per insert while AVL trees perform .7 rotations per insert resulting in a slightly shallower AVL tree; however, insert and lookup times are still extremely close.
