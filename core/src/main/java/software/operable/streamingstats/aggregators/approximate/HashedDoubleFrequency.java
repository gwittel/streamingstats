package software.operable.streamingstats.aggregators.approximate;

import com.clearspring.analytics.hash.MurmurHash;
import com.google.common.annotations.Beta;
import software.operable.streamingstats.aggregators.DoubleFrequency;

/**
 * Interface for hash based item frequency estimators
 */
@Beta
public interface HashedDoubleFrequency
        extends DoubleFrequency, BaseHashedFrequency
{
    /**
     * Take in, hash, then record item.
     *
     * @param t item to add
     */
    @Override
    default void accept(double t)
    {
        addHashed(hash(t), 1);
    }

    /**
     * Generic object hash to long method.
     * <p>
     * Relies on inbuilt double -> long (bits) -> hash(x) handlers
     */
    default long hash(double value)
    {
        return MurmurHash.hashLong(Double.doubleToLongBits(value));
    }

    /**
     * Retrieve estimated frequency of value
     */
    @Override
    default long get(double value)
    {
        return getHashed(hash(value));
    }
}
