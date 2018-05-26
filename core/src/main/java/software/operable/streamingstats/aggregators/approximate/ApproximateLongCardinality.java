package software.operable.streamingstats.aggregators.approximate;

import com.clearspring.analytics.hash.MurmurHash;
import com.clearspring.analytics.stream.cardinality.CardinalityMergeException;
import com.clearspring.analytics.stream.cardinality.ICardinality;
import com.google.common.base.MoreObjects;
import software.operable.streamingstats.aggregators.LongCardinality;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Track approximate cardinality of a series of longs.
 */
public class ApproximateLongCardinality implements LongCardinality
{
    private volatile ICardinality delegate;

    ApproximateLongCardinality()
    {
        this.delegate = Approximators.defaultHll();
    }

    public static ApproximateLongCardinality create()
    {
        return new ApproximateLongCardinality();
    }

    @Override
    public long get()
    {
        return delegate.cardinality();
    }

    @Override
    public LongCardinality mergeWith(LongCardinality other)
    {
        requireNonNull(other, "other is null");
        checkArgument(other instanceof ApproximateLongCardinality, "Cannot merge Cardinality estimators of differing types");

        try {
            this.delegate = delegate.merge(((ApproximateLongCardinality) other).delegate);
        }
        catch (CardinalityMergeException e) {
            throw new IllegalArgumentException("Problem merging estimators", e);
        }

        return this;
    }

    @Override
    public void accept(long value)
    {
        delegate.offer(MurmurHash.hashLong(value));
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("delegate", delegate)
                .toString();
    }
}
