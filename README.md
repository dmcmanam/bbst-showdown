# bbst-showdown
Performance of WAVL, AVL &amp; Red-black trees compared in Java.

Because AVL trees enforce stricter balance requirements than red-black trees, performance of AVL trees is substantially better when sequential elements are inserted and
nearly identical for random insertions.  'AVL tree implementations were consistently faster than red-black trees, by up to 20%' in the detailed 2004
analysis by Ben Pfaff-
https://benpfaff.org/papers/libavl.pdf
Comparing implementations from this project, the difference can be over 30% - 
https://refactoringlightly.wordpress.com/

What is fascinating about AVL trees is that although they were originally described in a paper from 1962, interesting variations were published in 2015(WAVL) and 2016(RAVL),
http://sidsen.azurewebsites.net//
so there remains a great coding opportunity to elegantly implement and explore the possibilities described in recent papers.

Specifically, rank balanced AVL trees are still not common so this project explores their implementation and various common AVL tree implementations such as balance factor.

Balanced binary trees such as red-black, AVL and RAVL can have a reputation for being difficult to code, 
that may have been the case in the 90s when coding them from a textbook or a research paper but with modern tools and resources the difficulty has decreased.
Take a look at the fixAfterInsert() method for TreeMapRAVL which uses rank and rank difference to build an AVL tree for an example of a fairly simple insert retracing loop.

## The AVL vs. Red-black Tree History

Unfortunately, red-black trees are the default implementation in Java, the Linux kernel and the C++ standard library.  Why? For random inserts, 
AVL trees perform .7 rotations per insert while red-black trees perform .6 rotations per insert.  Therefore, in a 1990s world of costly memory
access it was commonly ASSUMED that red-black trees would perform better for insert intensive tasks while AVL would perform better on lookup intensive
tasks. This type of statement, totally based on assumptions about the cost of rotations, is still common within many internet discussion groups.
On modern computers, rotations are a poor metric for performance - tree height, average node height and comparisons required per operation are much 
better benchmarks.  Also, the popular CLR algorithms textbook does not cover AVL trees, an issue I expect they will correct in future editions given 
current research.

I welcome your input on this project.