package software.operable.streamingstats.aggregators.approximate;

import com.google.common.base.MoreObjects;
import com.tdunning.math.stats.TDigest;
import software.operable.streamingstats.aggregators.NumericDistribution;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * This tracks the approximate distribution (ntile) of inserted values.
 *
 * Underneath it uses a t-digest type data structure to approximate the numeric
 * distribution of inserted elements.
 */
public class ApproximateDistribution
        implements NumericDistribution
{
    private final TDigest instance;

    ApproximateDistribution()
    {
        this.instance = TDigest.createDigest(100);
    }

    public static ApproximateDistribution create()
    {
        return new ApproximateDistribution();
    }

    @Override
    public double cdf(double cutoff)
    {
        return instance.cdf(cutoff);
    }

    @Override
    public double quantile(double quantile)
    {
        return instance.quantile(quantile);
    }

    @Override
    public void accept(double value)
    {
        instance.add(value);
    }

    @Override
    public void accept(int value)
    {
        instance.add(value);
    }

    @Override
    public void accept(long value)
    {
        instance.add(value);
    }

    @Override
    public NumericDistribution mergeWith(NumericDistribution other)
    {
        requireNonNull(other, "other is null");
        checkArgument(other instanceof ApproximateDistribution, "other is not a NumericDistribution");
        checkArgument(((ApproximateDistribution) other).instance instanceof TDigest, "other does not use the same underlying implementation");
        instance.add(((ApproximateDistribution) other).instance);

        return this;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("instance", instance)
                .toString();
    }
}
