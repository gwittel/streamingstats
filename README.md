# Description

The main goal of this project is to simplify counting things.  In many cases we want to gather various
statistics on the same series of items where such series are too large to hold in memory.

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

  
# Target Usage Patterns

```java
// Simple - Full Stream Consumption
long estimatedUniqueItems = Approximators.cardinalityOf(someStream)

Frequency<String> stringFreq = Approximators.frequencyOf(someStringStream);
long estimatedHelloCount = stringFreq.get("hello");

NumericDistribution distribution = Approximators.distributionOf(someNumberStream);
double median = distribution.quantile(0.5);
```


# Status

Extremely early.  Likely that large things will change including breaking API changes.

This is a side project and my time is limited.

## Rough Feature Roadmap

1. Incorporate more helpers to aggregate multiple types of statistics (likely a separate module using RX / JEP-266).
   1. Simple stream accumulation to output.
   2. Time windowed emission to subscribers.
2. Algorithms
   1. Refine selection/use of underlying implementations.
   2. Add probabilistic TopK.
   3. Expose parameter selection based on expected input scale.
3. Refactor to drop Java 9 requirement for some features.  Reactive features driven by JEP-266 might come in through a separate sub module.

# TODO

- [x] Integrate stream processing helpers like combiners where aggregation output type clear (e.g. cardinality).
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
- [ ] Cleanup and push to Maven Central
- [ ] Bring in Java 9 JEP-266 for simplified pub/sub model
- [ ] Consider removing unnecessary abstractions and adopt different stats libs like `airlift/stats` (at cost of more dependencies).
- [ ] Change `add` type operations to be chainable.
- [ ] Decide if `Aggregator` methods must or must not tolerate null inputs.
- [ ] Consider alternate HLL - http://dsiutils.di.unimi.it/ . Good for having large numbers of HLLs due to reduced object creation overhead

# Known Issues

- The `mergeWith` functionality needs thinking.  What's the point of having a merge on an interface when you can't mingle/merge implementations?  Feels hacky.
- Thread safety.  We need to go through implementations and verify safety (or not) and mark limitations accordingly.
    - Based on this we'll need to update the flags to the collectors in the `Approximators` class.

# Contributing

Nothing much yet.  Pull requests welcome.

Please format code based on code format from [airlift/codestyle](https://github.com/airlift/codestyle)
