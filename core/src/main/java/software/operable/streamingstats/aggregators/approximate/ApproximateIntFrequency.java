package software.operable.streamingstats.aggregators.approximate;

import com.clearspring.analytics.stream.frequency.CountMinSketch;
import com.clearspring.analytics.stream.frequency.FrequencyMergeException;
import software.operable.streamingstats.aggregators.IntFrequency;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * This tracks the approximate frequency of ints inserted within a bound percent error.
 * <p>
 * Underneath it is using a Count-min-sketch algorithm to do so.
 */
public class ApproximateIntFrequency implements HashedIntFrequency
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

    ApproximateIntFrequency()
    {
        this.instance = new CountMinSketch(EPSILON, CONFIDENCE, SEED);
    }

    public static ApproximateIntFrequency create()
    {
        return new ApproximateIntFrequency();
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
    public IntFrequency mergeWith(IntFrequency other)
    {
        requireNonNull(other, "other is null");
        checkArgument(other instanceof ApproximateIntFrequency, "Cannot merge frequency estimators of differing types.");

        // The underlying implementation won't let you merge into one for some reason
        // TODO Will an alternate implementation resolve this inconsistency?
        try {
            this.instance = CountMinSketch.merge(instance, ((ApproximateIntFrequency) other).instance);
        }
        catch (FrequencyMergeException e) {
            throw new IllegalArgumentException("Problem merging estimators", e);
        }

        return this;
    }
}
