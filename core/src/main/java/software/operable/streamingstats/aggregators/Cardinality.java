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

import software.operable.streamingstats.Aggregator;

/**
 * Track number of unique items across consumed input.
 *
 * @param <T> Type of item to track.
 */
public interface Cardinality<T>
        extends Aggregator<T>
{
    /**
     * Get the number of unique items added/accepted.
     *
     * @return
     */
    long get();

    /**
     * Merge data from {@code other} into the current aggregator.
     *
     * @param other
     * @return The mutated {@code Cardinality<T>} object
     */
    Cardinality<T> mergeWith(Cardinality<T> other);
}
