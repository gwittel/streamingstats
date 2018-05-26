package software.operable.streamingstats.aggregators.approximate;

import com.clearspring.analytics.hash.MurmurHash;
import com.google.common.annotations.Beta;
import software.operable.streamingstats.aggregators.IntFrequency;

/**
 * Interface for hash based item frequency estimators
 */
@Beta
public interface HashedIntFrequency
        extends IntFrequency, BaseHashedFrequency
{
    /**
     * Take in, hash, then record item.
     *
     * @param t item to add
     */
    @Override
    default void accept(int t)
    {
        addHashed(hash(t), 1);
    }

    /**
     * Generic object hash to long method.
     * <p>
     * Relies on inbuilt int -> long -> hash(x) handlers
     */
    default long hash(int value)
    {
        return MurmurHash.hashLong(value);
    }

    /**
     * Retrieve estimated frequency of value
     */
    @Override
    default long get(int value)
    {
        return getHashed(hash(value));
    }
}
