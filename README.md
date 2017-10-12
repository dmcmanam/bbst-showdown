# bbst-showdown
Performance of AVL &amp; Red-black trees compared in Java.

Because AVL trees enforce stricter balance requirements than red-black trees, performance of AVL trees is better than red-black in situations where 
red-black trees become unbalanced such as inserting 1 million elements in-order.

For example, consider the following mean times for inserting integers in-order compared to OpenJDK’s JDK 1.8 TreeMap:

| Tree Type  | 100,000 inserts | 1,000,000 inserts |
| --- | ---: | ---:|
| AVL  | 36ms  | 192ms |
| Red-black  | 54ms  | 323ms |

Balanced binary trees such as red-black, AVL and WAVL can have a reputation for being difficult to code, when each tree transformation necessary for insert and delete is
drawn out then it becomes clear that the code will be quick to implement.

I welcome your input on this project.