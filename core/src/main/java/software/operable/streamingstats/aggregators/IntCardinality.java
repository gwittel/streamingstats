package software.operable.streamingstats.aggregators;

import com.google.common.annotations.Beta;
import software.operable.streamingstats.IntAggregator;

/**
 * Track number of unique items across consumed input.
 * <p>
 * For other primitive types, see {@link LongCardinality}, and {@link DoubleCardinality}.
 * For non-primitives, see {@link Cardinality}.
 */
@Beta
public interface IntCardinality
        extends IntAggregator
{
    /**
     * Get the number of unique items added/accepted.
     */
    long get();

    /**
     * Merge data from {@code other} into the current aggregator.
     *
     * @return The mutated {@code IntCardinality} object
     */
    IntCardinality mergeWith(IntCardinality other);
}
