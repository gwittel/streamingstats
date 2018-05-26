package software.operable.streamingstats.aggregators;

import com.google.common.annotations.Beta;
import software.operable.streamingstats.DoubleAggregator;

/**
 * Track frequency of items within consumed input.
 */
@Beta
public interface DoubleFrequency
        extends DoubleAggregator
{
    /**
     * Get the frequency of the given {@code value}.
     *
     * @return Frequency of the item.
     */
    long get(double value);

    /**
     * Merge data from {@code other} into the current aggregator.
     *
     * @return The mutated {@code DoubleFrequency} object.
     */
    DoubleFrequency mergeWith(DoubleFrequency other);
}
