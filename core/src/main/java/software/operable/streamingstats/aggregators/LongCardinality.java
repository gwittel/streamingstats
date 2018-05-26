package software.operable.streamingstats.aggregators;

import com.google.common.annotations.Beta;
import software.operable.streamingstats.LongAggregator;

/**
 * Track number of unique items across consumed input.
 * <p>
 * For other primitive types, see {@link IntCardinality}, and {@link DoubleCardinality}.
 * For non-primitives, see {@link Cardinality}.
 */
@Beta
public interface LongCardinality
        extends LongAggregator
{
    /**
     * Get the number of unique items added/accepted.
     */
    long get();

    /**
     * Merge data from {@code other} into the current aggregator.
     *
     * @return The mutated {@code LongCardinality} object
     */
    LongCardinality mergeWith(LongCardinality other);
}
