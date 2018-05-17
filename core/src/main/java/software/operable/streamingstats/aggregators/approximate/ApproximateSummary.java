package software.operable.streamingstats.aggregators.approximate;

import com.google.common.base.MoreObjects;
import software.operable.streamingstats.aggregators.Cardinality;
import software.operable.streamingstats.aggregators.Frequency;
import software.operable.streamingstats.aggregators.Summary;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Track multiple approximate metrics across a set of data.
 *
 * @param <T> Type of items to track.
 */
public class ApproximateSummary<T> implements Summary<T>
{
    private long size;

    protected final Cardinality<T> approximateCardinality;
    protected final Frequency<T> approximateFrequency;

    ApproximateSummary()
    {
        size = 0;
        approximateCardinality = Approximators.cardinality();
        approximateFrequency = Approximators.frequency();
    }

    public static <T> ApproximateSummary<T> create()
    {
        return new ApproximateSummary<>();
    }

    /**
     * @return The exact number of items added to this data structure (vs approximated).
     */
    @Override
    public long size()
    {
        return size;
    }

    /**
     * Get the estimated set cardinality of the items added.
     */
    @Override
    public long cardinality()
    {
        return approximateCardinality.get();
    }

    /**
     * Get the estimated frequency of the givem item.
     *
     * @param value Item to look up
     * @return estimated frequency of the item
     */
    @Override
    public long frequency(T value)
    {
        return approximateFrequency.get(value);
    }

    @Override
    public Summary<T> mergeWith(Summary<T> other)
    {
        requireNonNull(other, "other is null");
        checkArgument(other instanceof ApproximateSummary, "Cannot merge Summary estimators of differing types.");

        approximateCardinality.mergeWith(((ApproximateSummary) other).approximateCardinality);
        approximateFrequency.mergeWith(((ApproximateSummary) other).approximateFrequency);

        size += other.size();

        return this;
    }

    @Override
    public void accept(T t)
    {
        approximateCardinality.accept(t);
        approximateFrequency.accept(t);
        size++;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("size", size)
                .add("approximateCardinality", approximateCardinality)
                .add("approximateFrequency", approximateFrequency)
                .toString();
    }
}
