package software.operable.streamingstats.aggregators;

import com.google.common.annotations.Beta;
import software.operable.streamingstats.NumericAggregator;

/**
 * Track both cardinality and frequency of elements.
 */
@Beta
public interface NumericSummary extends NumericAggregator
{
    /**
     * Get the number of items added/accepted.
     */
    long size();

    /**
     * Get the number of unique items added/accepted.
     */
    long cardinality();

    /**
     * Get the frequency of the given {@code value}.
     *
     * @return Frequency of the item.
     */
    long frequency(double value);

    /**
     * Get the value at the given {@code cutoff} +/- some maximum error rate.
     *
     * @param cutoff Cumulative distribution cutoff value.
     * @return The approximate fraction of all samples whose value is less than or equal to {@code cutoff}
     */
    double cdf(double cutoff);

    /**
     * Get the value at the given {@code quantile} +/- some maximum error rate.  Must be in range [0, 1]
     *
     * @param quantile The quantile desired. Must be in range [0.0, 1.0]
     * @return The smallest value x such that x percent of samples are less than or equal to {@code quantile}.
     */
    double quantile(double quantile);

    /**
     * Merge data from {@code other} into the current a NumericSummary aggregator.
     *
     * @return The mutated {@link NumericSummary} object
     */
    NumericSummary mergeWith(NumericSummary other);
}
