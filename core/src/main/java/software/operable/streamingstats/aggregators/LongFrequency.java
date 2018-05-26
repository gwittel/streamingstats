package software.operable.streamingstats.aggregators;

import com.google.common.annotations.Beta;
import software.operable.streamingstats.LongAggregator;

/**
 * Track frequency of items within consumed input.
 */
@Beta
public interface LongFrequency
        extends LongAggregator
{
    /**
     * Get the frequency of the given {@code value}.
     *
     * @return Frequency of the item.
     */
    long get(long value);

    /**
     * Merge data from {@code other} into the current aggregator.
     *
     * @return The mutated {@code LongFrequency} object.
     */
    LongFrequency mergeWith(LongFrequency other);
}
