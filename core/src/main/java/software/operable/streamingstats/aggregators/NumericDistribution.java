package software.operable.streamingstats.aggregators;

import com.google.common.annotations.Beta;
import software.operable.streamingstats.NumericAggregator;

/**
 * Track distribution of numeric elements and retrieve items by quantile.
 */
@Beta
public interface NumericDistribution
        extends NumericAggregator
{
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
     * Merge another NumericDistribution INTO the current instance
     * @param other
     * @return The mutated {@code NumericDistribution} object
     */
    NumericDistribution mergeWith(NumericDistribution other);
}
