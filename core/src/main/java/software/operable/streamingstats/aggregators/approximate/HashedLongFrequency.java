package software.operable.streamingstats.aggregators.approximate;

import com.clearspring.analytics.hash.MurmurHash;
import com.google.common.annotations.Beta;
import software.operable.streamingstats.aggregators.LongFrequency;

/**
 * Interface for hash based item frequency estimators
 */
@Beta
public interface HashedLongFrequency
        extends LongFrequency, BaseHashedFrequency
{
    /**
     * Take in, hash, then record item.
     *
     * @param t item to add
     */
    @Override
    default void accept(long t)
    {
        addHashed(hash(t), 1);
    }

    /**
     * Generic object hash to long method.
     * <p>
     * Relies on inbuilt hash(x) handlers
     */
    default long hash(long value)
    {
        return MurmurHash.hashLong(value);
    }

    /**
     * Retrieve estimated frequency of value
     */
    @Override
    default long get(long value)
    {
        return getHashed(hash(value));
    }
}
