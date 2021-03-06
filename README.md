

# Deeper understanding of vector clocks and Implementation :roller_skate:

## Introduction :sweat_smile:

- Vector clock is the main mechanism by which partial order is established in distributed systems where central synchronization is not possible. It is the foundation of other distributed system data structures.
- In order to better understanding the underlying algorithm, the seminal paper, a implementation is beneficial.

## Code structure :ice_cube:
- Snapshots: Records all event snapshots for later comparison.
- Vectors: Executes the Vector Clock algorithm.
- Node:initiate events. Send and receive event messages to and from queue. 
- MessageQueue: Facilitates inter-node communication, ran from independent thread.
- Api: contains methods to compare and return order of two events.

## Test :bellhop_bell:
- Write API to interact with vector clock algorithm.
- Demonstrate vector clock algorithm through simulation.
- Execute events in sequence and validate correctness by checking timestamp vector generated by the algorithm:
- Test details:
  - Send, Receive, Internal EVENTS
  - Determine partial order through timestamps: Concurrent, happened before(or after).



## References

- http://guyharrison.squarespace.com/blog/2015/10/12/vector-clocks.html
- Logical clocks: https://people.cs.rutgers.edu/~pxk/417/notes/logical-clocks.html
- https://sookocheff.com/post/time/vector-clocks/
- https://www.cs.uic.edu/~ajayk/Chapter3.pdf

> Cheng Shen
> Apr 24, 2022
