package software.operable.streamingstats.aggregators;

import com.google.common.annotations.Beta;
import software.operable.streamingstats.DoubleAggregator;

/**
 * Track number of unique items across consumed input.
 * <p>
 * For other primitive types, see {@link IntCardinality}, and {@link LongCardinality}.
 * For non-primitives, see {@link Cardinality}.
 */
@Beta
public interface DoubleCardinality
        extends DoubleAggregator
{
    /**
     * Get the number of unique items added/accepted.
     */
    long get();

    /**
     * Merge data from {@code other} into the current aggregator.
     *
     * @return The mutated {@code DoubleCardinality} object
     */
    DoubleCardinality mergeWith(DoubleCardinality other);
}
