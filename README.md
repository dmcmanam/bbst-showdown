# bbst-showdown
Performance of WAVL, AVL &amp; Red-black trees compared in Java.

Because AVL trees enforce stricter balance requirements than red-black trees, performance of AVL trees is better than red-black in situations where 
red-black trees become unbalanced such as inserting 1 million elements in-order.

There are excellent resources for AVL trees in the c language.  Two of them are:
https://benpfaff.org/papers/libavl.pdf
http://www.eternallyconfuzzled.com/tuts/datastructures/jsw_tut_avl.aspx

However, I have been unable to find a variety of AVL tree implementations in Java.
Specifically, most Java implementations use height and recursion, arguably the most elegant; 
however, I wanted to implement a faster version with parent pointers, balance factors and no recursion.

Comparing in-order insertions with OpenJDK’s JDK 1.8 TreeMap and this AVL implementation with parent pointers, the difference is notable:

| Tree Type  | 100,000 inserts | 1,000,000 inserts |
| --- | ---: | ---:|
| AVL  | 36ms  | 192ms |
| Red-black  | 54ms  | 323ms |

Balanced binary trees such as red-black, AVL and WAVL can have a reputation for being difficult to code, 
when each tree transformation necessary for insert and delete is
drawn out then it becomes clear that the code will be quick to implement.

## The AVL vs. Red-black Tree History

Unfortunately, red-black trees are the default implementation in Java, the Linux kernel and the C++ standard library.  Why? For random inserts, 
AVL trees perform .7 rotations per insert while red-black trees perform .6 rotations per insert.  Therefore, in a 1990s world of costly memory
access it was commonly ASSUMED that red-black trees would perform better for insert intensive tasks while AVL would perform better on lookup intensive
tasks. This type of statement, totally based on assumptions about the cost of rotations, is still common within many internet discussion groups.

I welcome your input on this project.