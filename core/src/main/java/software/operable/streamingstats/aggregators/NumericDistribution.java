/**
 * Copyright 2017 Greg Wittel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package software.operable.streamingstats.aggregators;

import software.operable.streamingstats.NumericAggregator;

/**
 * Track distribution of elements and retrieve items by quantile.
 */
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
