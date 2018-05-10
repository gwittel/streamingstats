/*
 * Copyright 2017 Greg Wittel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package software.operable.streamingstats.aggregators.approximate;

import com.clearspring.analytics.stream.frequency.CountMinSketch;
import com.clearspring.analytics.stream.frequency.FrequencyMergeException;
import com.google.common.base.MoreObjects;
import software.operable.streamingstats.aggregators.Frequency;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * This tracks the approximate frequency of items inserted within a bound percent error.
 * <p>
 * Unearneath it is using a Count-min-sketch algorithm to do so.
 */
public class ApproximateFrequency<T>
        implements HashedFrequency<T>
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

    ApproximateFrequency()
    {
        this.instance = new CountMinSketch(EPSILON, CONFIDENCE, SEED);
    }

    public static <T> ApproximateFrequency<T> create()
    {
        return new ApproximateFrequency<>();
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
    public Frequency<T> mergeWith(Frequency<T> other)
    {
        requireNonNull(other, "other is null");
        checkArgument(other instanceof ApproximateFrequency, "Cannot merge frequency estimators of differing types.");

        // The underlying implementation won't let you merge into one for some reason
        // TODO Will an alternate implementation resolve this inconsistency?
        try {
            this.instance = CountMinSketch.merge(instance, ((ApproximateFrequency) other).instance);
        }
        catch (FrequencyMergeException e) {
            throw new IllegalArgumentException("Problem merging estimators", e);
        }

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
