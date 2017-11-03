# Description

# Goals

The main goal of this project is to simplify counting things.  In many cases we want to gather various
statistics on the same series of items.

We want to:

1. Provide concise ways to track mulitple statistics on a stream of data.
2. Provide reasonable default implementations for aggregation of the statistics.

In particular, the focus is on estimating statistics across large data streams.  On such streams, it
is often not possible to maintain exact counts/statistics due to memory limitations.  Instead we rely on a class of data structures 
referred to as probabilistic data structures.  Common examples include
[HyperLogLog](https://en.wikipedia.org/wiki/HyperLogLog) (for estimating set cardinality), and [Count Min Sketch](https://en.wikipedia.org/wiki/Count%E2%80%93min_sketch) (for estimating item frequency).

When using these data structures, there are various tradeoffs to be made.  Some of these include:

* Benefits
  * Able to estimate over datasets too large to fit in memory.
  * Generally low memory requirements.
  
* Costs
  * An estimated (possibly bounded) error rate.
  * Possibly higher compute cost to add elements.
  * Ability to combine multiple estimators (e.g. two HyperLogLog structures) is restricted.
  
# Target Usage Pattern

```java
// TODO Document sample usage
```

# TODO

1. Integrate stream processing helpers like combiners where aggregation output type clear (e.g. cardinality).
    ```java 
    // Cardinality with finisher
    Collectors.of(
            CardinalityImpl::new,
            CardinalityImpl::add,
            CardinalityImpl::mergeWith,
            CardinalityImpl::get()
    );
    // Cardinality wihtout is similar except no finisher (identity)
    
    // Quantile -- requires fixed quantile or identity finisher
    ```
2. Bring in Java 9 JEP-266 for simplified pub/sub model
3. Consider removing unnecessary abstractions and adopt different stats libs like `airlift/stats` (at cost of more dependencies).
4. Change `add` type operations to be chainable.
5. Decide if `Aggregator` methods must or must not tolerate null inputs.