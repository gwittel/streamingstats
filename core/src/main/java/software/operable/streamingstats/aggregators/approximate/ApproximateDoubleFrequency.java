package software.operable.streamingstats.aggregators.approximate;

import com.clearspring.analytics.stream.frequency.CountMinSketch;
import com.clearspring.analytics.stream.frequency.FrequencyMergeException;
import software.operable.streamingstats.aggregators.DoubleFrequency;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * This tracks the approximate frequency of doubles inserted within a bound percent error.
 * <p>
 * Underneath it is using a Count-min-sketch algorithm to do so.
 */
public class ApproximateDoubleFrequency implements HashedDoubleFrequency
{
    /**
     * Tolerated percent error
     */
    private static final double EPSILON = 0.0001;

    /**
     * Desired probability of accurate count
     */
    private static final double CONFIDENCE = 0.999;

    private static final int SEED = 1;

    private volatile CountMinSketch instance; // volatile due to limitation in how CMS objects are merged in the implementation library

    ApproximateDoubleFrequency()
    {
        this.instance = new CountMinSketch(EPSILON, CONFIDENCE, SEED);
    }

    public static ApproximateDoubleFrequency create()
    {
        return new ApproximateDoubleFrequency();
    }

    @Override
    public void addHashed(long hashedValue, long count)
    {
        instance.add(hashedValue, count);
    }

    @Override
    public long getHashed(long hashedValue)
    {
        return instance.estimateCount(hashedValue);
    }

    @Override
    public DoubleFrequency mergeWith(DoubleFrequency other)
    {
        requireNonNull(other, "other is null");
        checkArgument(other instanceof ApproximateDoubleFrequency, "Cannot merge frequency estimators of differing types.");

        // The underlying implementation won't let you merge into one for some reason
        // TODO Will an alternate implementation resolve this inconsistency?
        try {
            this.instance = CountMinSketch.merge(instance, ((ApproximateDoubleFrequency) other).instance);
        }
        catch (FrequencyMergeException e) {
            throw new IllegalArgumentException("Problem merging estimators", e);
        }

        return this;
    }
}
