package software.operable.streamingstats.aggregators.approximate;

import com.clearspring.analytics.stream.cardinality.CardinalityMergeException;
import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import com.clearspring.analytics.stream.cardinality.ICardinality;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import software.operable.streamingstats.aggregators.Cardinality;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * This tracks the approximate set cardinality (number of unique items).
 *
 * Underneath, it is using a HyperLogLog counter to track the data.  It will return the number
 * of unique items inserted within a bound percent error.
 *
 * @param <T>
 */
public class ApproximateCardinality<T>
        implements Cardinality<T>
{

    // TODO Consider alternate HLL - http://dsiutils.di.unimi.it/
    // Good for having large numbers of HLLs due to reduced object creation overhead
    private volatile ICardinality delegate;

    ApproximateCardinality()
    {
        this.delegate = defaultHll();
    }

    public static <T> ApproximateCardinality<T> create()
    {
        return new ApproximateCardinality<>();
    }

    @Override
    public long get()
    {
        return delegate.cardinality();
    }

    @Override
    public Cardinality<T> mergeWith(Cardinality<T> other)
    {
        requireNonNull(other, "other is null");
        checkArgument(other instanceof ApproximateCardinality, "Cannot merge Cardinality estimators of differing types");

        try {
            this.delegate = delegate.merge(((ApproximateCardinality) other).delegate);
        }
        catch (CardinalityMergeException e) {
            throw new IllegalArgumentException("Problem merging estimators", e);
        }

        return this;
    }

    @Override
    public void accept(T t)
    {
        delegate.offer(t);
    }

    @VisibleForTesting
    HyperLogLogPlus defaultHll()
    {
        return new HyperLogLogPlus(14, 32);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("delegate", delegate)
                .toString();
    }
}
