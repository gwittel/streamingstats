package software.operable.streamingstats.aggregators;

import com.google.common.annotations.Beta;
import software.operable.streamingstats.IntAggregator;

/**
 * Track frequency of items within consumed input.
 */
@Beta
public interface IntFrequency
        extends IntAggregator
{
    /**
     * Get the frequency of the given {@code value}.
     *
     * @return Frequency of the item.
     */
    long get(int value);

    /**
     * Merge data from {@code other} into the current aggregator.
     *
     * @return The mutated {@code IntFrequency} object.
     */
    IntFrequency mergeWith(IntFrequency other);
}
