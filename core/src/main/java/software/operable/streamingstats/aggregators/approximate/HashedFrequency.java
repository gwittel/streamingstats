package software.operable.streamingstats.aggregators.approximate;

import com.clearspring.analytics.hash.MurmurHash;
import com.google.common.annotations.Beta;
import software.operable.streamingstats.aggregators.Frequency;

/**
 * Interface for hash based item frequency estimators
 *
 * @param <T> Type of item to track.
 */
@Beta
public interface HashedFrequency<T>
        extends Frequency<T>
{

    /**
     * Add a pre-hashed value into the frequency aggregator data structure.
     *
     * @param hashedValue Value resulting from the implemented {@link #hash(T)} function.
     * @param count Amount to increment frequency by.
     */
    void addHashed(long hashedValue, long count);

    /**
     * Retrieve the frequency of a hashed value
     *
     * @param hashedValue Value resulting from the implemented {@link #hash(T)} function.
     * @return Frequency of the {@code hashedValue}
     */
    long getHashed(long hashedValue);

    /**
     * Take in, hash, then record item.
     *
     * @param t item to add
     */
    @Override
    default void accept(T t)
    {
        addHashed(hash(t), 1);
    }

    /**
     * Generic object hash to long method.
     * <p>
     * Relies on inbuilt Object -> bytes -> hash(x) handlers
     */
    default long hash(T value)
    {
        return MurmurHash.hash(value);
    }

    /**
     * Retrieve estimated frequency of value
     */
    @Override
    default long get(T value)
    {
        return getHashed(hash(value));
    }
}
