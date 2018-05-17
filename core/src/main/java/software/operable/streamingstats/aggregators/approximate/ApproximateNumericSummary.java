package software.operable.streamingstats.aggregators.approximate;

import software.operable.streamingstats.aggregators.NumericDistribution;
import software.operable.streamingstats.aggregators.NumericSummary;
import software.operable.streamingstats.aggregators.Summary;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

public class ApproximateNumericSummary implements NumericSummary
{
    private long size;

    protected final Summary<Double> summary; // TODO Specialize so we don't have to box everything
    protected final NumericDistribution approximateDistribution;

    ApproximateNumericSummary()
    {
        summary = ApproximateSummary.create();
        approximateDistribution = Approximators.distribution();
    }

    public static ApproximateNumericSummary create()
    {
        return new ApproximateNumericSummary();
    }

    @Override
    public long size()
    {
        return size;
    }

    @Override
    public long cardinality()
    {
        return summary.cardinality();
    }

    @Override
    public long frequency(double value)
    {
        return summary.frequency(value);
    }

    @Override
    public double cdf(double cutoff)
    {
        return approximateDistribution.cdf(cutoff);
    }

    @Override
    public double quantile(double quantile)
    {
        return approximateDistribution.quantile(quantile);
    }

    @Override
    public NumericSummary mergeWith(NumericSummary other)
    {
        requireNonNull(other, "other is null");
        checkArgument(other instanceof ApproximateNumericSummary, "Cannot merge NumericSummary estimators of differing types.");

        summary.mergeWith(((ApproximateNumericSummary) other).summary);
        approximateDistribution.mergeWith(((ApproximateNumericSummary) other).approximateDistribution);

        size += other.size();

        return this;
    }

    @Override
    public void accept(double value)
    {
        summary.accept(value);
        approximateDistribution.accept(value);
    }

    @Override
    public void accept(int value)
    {
        accept((double) value);
    }

    @Override
    public void accept(long value)
    {
        accept((double) value);
    }
}
