package software.operable.streamingstats.aggregators;

import com.google.common.annotations.Beta;
import software.operable.streamingstats.Aggregator;

/**
 * Track frequency of items within consumed input.
 *
 * @param <T> Type of item to track.
 */
@Beta
public interface Frequency<T>
        extends Aggregator<T>
{
    /**
     * Get the frequency of the given {@code value}.
     *
     * @return Frequency of the item.
     */
    long get(T value);

    /**
     * Merge data from {@code other} into the current aggregator.
     *
     * @return The mutated {@code Frequency<T>} object.
     */
    Frequency<T> mergeWith(Frequency<T> other);
}
