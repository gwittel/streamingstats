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

import com.clearspring.analytics.hash.MurmurHash;
import com.google.common.annotations.Beta;
import software.operable.streamingstats.aggregators.Frequency;

/**
 * Interface for hash based item frequency estimators
 *
 * @param <T> Type of item to track.
 */
@Beta
public interface HashedFrequency<T>
        extends Frequency<T>
{

    /**
     * Add a pre-hashed value into the frequency aggregator data structure.
     *
     * @param hashedValue Value resulting from the implemented {@link #hash(T)} function.
     * @param count Amount to increment frequency by.
     */
    void addHashed(long hashedValue, long count);

    /**
     * Retrieve the frequency of a hashed value
     *
     * @param hashedValue Value resulting from the implemented {@link #hash(T)} function.
     * @return Frequency of the {@code hashedValue}
     */
    long getHashed(long hashedValue);

    /**
     * Take in, hash, then record item.
     *
     * @param t item to add
     */
    @Override
    default void accept(T t)
    {
        addHashed(hash(t), 1);
    }

    /**
     * Generic object hash to long method.
     * <p>
     * Relies on inbuilt Object -> bytes -> hash(x) handlers
     */
    default long hash(T value)
    {
        return MurmurHash.hash(value);
    }

    /**
     * Retrieve estimated frequency of value
     */
    @Override
    default long get(T value)
    {
        return getHashed(hash(value));
    }
}
