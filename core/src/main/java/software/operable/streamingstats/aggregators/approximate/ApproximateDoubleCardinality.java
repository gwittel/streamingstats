package software.operable.streamingstats.aggregators.approximate;

import com.clearspring.analytics.hash.MurmurHash;
import com.clearspring.analytics.stream.cardinality.CardinalityMergeException;
import com.clearspring.analytics.stream.cardinality.ICardinality;
import com.google.common.base.MoreObjects;
import software.operable.streamingstats.aggregators.DoubleCardinality;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Track approximate cardinality of a series of doubles.
 */
public class ApproximateDoubleCardinality implements DoubleCardinality
{
    private volatile ICardinality delegate;

    ApproximateDoubleCardinality()
    {
        this.delegate = Approximators.defaultHll();
    }

    public static ApproximateDoubleCardinality create()
    {
        return new ApproximateDoubleCardinality();
    }

    @Override
    public long get()
    {
        return delegate.cardinality();
    }

    @Override
    public DoubleCardinality mergeWith(DoubleCardinality other)
    {
        requireNonNull(other, "other is null");
        checkArgument(other instanceof ApproximateDoubleCardinality, "Cannot merge Cardinality estimators of differing types");

        try {
            this.delegate = delegate.merge(((ApproximateDoubleCardinality) other).delegate);
        }
        catch (CardinalityMergeException e) {
            throw new IllegalArgumentException("Problem merging estimators", e);
        }

        return this;
    }

    @Override
    public void accept(double value)
    {
        delegate.offer(MurmurHash.hashLong(Double.doubleToLongBits(value)));
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("delegate", delegate)
                .toString();
    }
}
