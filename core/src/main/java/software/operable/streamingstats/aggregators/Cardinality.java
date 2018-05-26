package software.operable.streamingstats.aggregators;

import com.google.common.annotations.Beta;
import software.operable.streamingstats.Aggregator;

/**
 * Track number of unique items across consumed input.
 *
 * @param <T> Type of item to track.
 */
@Beta
public interface Cardinality<T>
        extends Aggregator<T>
{
    /**
     * Get the number of unique items added/accepted.
     *
     * @return
     */
    long get();

    /**
     * Merge data from {@code other} into the current aggregator.
     *
     * @param other
     * @return The mutated {@code Cardinality<T>} object
     */
    Cardinality<T> mergeWith(Cardinality<T> other);
}
