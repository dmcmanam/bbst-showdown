# bbst-showdown
Performance of RAVL, AVL &amp; Red-black trees compared in Java.

Because AVL trees enforce stricter balance requirements than red-black trees, performance of AVL trees is substantially better when sequential elements are inserted and
nearly identical for random insertions.  If you are still using red-black trees and performance is important it is time to consider alternatives.

For additional reading see:
https://refactoringlightly.wordpress.com/

There are excellent resources for AVL trees in the c language.  Two of them are:
https://benpfaff.org/papers/libavl.pdf
http://www.eternallyconfuzzled.com/tuts/datastructures/jsw_tut_avl.aspx

However, performance oriented Java implementations of AVL trees were lacking so this project exists.

Balanced binary trees such as red-black, AVL and RAVL can have a reputation for being difficult to code, 
that may have been the case in the 90s when coding them from a textbook or a research paper but with modern tools and resources the difficulty has decreased.
Take a look at the fixAfterInsert() method for TreeMapRAVL which uses rank and rank difference to build an AVL tree for an example of a fairly simple insert retracing loop.

## The AVL vs. Red-black Tree History

Unfortunately, red-black trees are the default implementation in Java, the Linux kernel and the C++ standard library.  Why? For random inserts, 
AVL trees perform .7 rotations per insert while red-black trees perform .6 rotations per insert.  Therefore, in a 1990s world of costly memory
access it was commonly ASSUMED that red-black trees would perform better for insert intensive tasks while AVL would perform better on lookup intensive
tasks. This type of statement, totally based on assumptions about the cost of rotations, is still common within many internet discussion groups.
On modern computers, rotations are a poor metric for performance - tree height, average node height and comparisons required per operation are much 
better benchmarks.

I welcome your input on this project.