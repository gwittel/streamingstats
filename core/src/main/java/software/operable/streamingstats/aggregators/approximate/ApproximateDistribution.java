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
package software.operable.streamingstats.aggregators.approximate;

import com.google.common.base.MoreObjects;
import com.tdunning.math.stats.TDigest;
import software.operable.streamingstats.aggregators.NumericDistribution;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

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
