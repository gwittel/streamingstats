package software.operable.streamingstats.aggregators.approximate;

import com.clearspring.analytics.hash.MurmurHash;
import com.clearspring.analytics.stream.cardinality.CardinalityMergeException;
import com.clearspring.analytics.stream.cardinality.ICardinality;
import com.google.common.base.MoreObjects;
import software.operable.streamingstats.aggregators.IntCardinality;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Track approximate cardinality of a series of ints.
 */
public class ApproximateIntCardinality implements IntCardinality
{
    private volatile ICardinality delegate;

    ApproximateIntCardinality()
    {
        this.delegate = Approximators.defaultHll();
    }

    public static ApproximateIntCardinality create()
    {
        return new ApproximateIntCardinality();
    }

    @Override
    public long get()
    {
        return delegate.cardinality();
    }

    @Override
    public IntCardinality mergeWith(IntCardinality other)
    {
        requireNonNull(other, "other is null");
        checkArgument(other instanceof ApproximateIntCardinality, "Cannot merge Cardinality estimators of differing types");

        try {
            this.delegate = delegate.merge(((ApproximateIntCardinality) other).delegate);
        }
        catch (CardinalityMergeException e) {
            throw new IllegalArgumentException("Problem merging estimators", e);
        }

        return this;
    }

    @Override
    public void accept(int value)
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
