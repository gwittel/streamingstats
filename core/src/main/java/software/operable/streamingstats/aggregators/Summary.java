package software.operable.streamingstats.aggregators;

import software.operable.streamingstats.Aggregator;

/**
 * Track both cardinality and frequency of elements.
 *
 * @param <T> Type of items to track.
 */
public interface Summary<T> extends Aggregator<T>
{
    /**
     * Get the number of items added/accepted.
     */
    long size();

    /**
     * Get the number of unique items added/accepted.
     */
    long cardinality();

    /**
     * Get the frequency of the given {@code value}.
     *
     * @return Frequency of the item.
     */
    long frequency(T value);

    /**
     * Merge data from {@code other} into the current asummaryggregator.
     *
     * @return The mutated {@code Summary<T>} object
     */
    Summary<T> mergeWith(Summary<T> other);
}
