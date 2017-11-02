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

import software.operable.streamingstats.aggregators.Cardinality;
import software.operable.streamingstats.aggregators.Frequency;
import software.operable.streamingstats.aggregators.NumericDistribution;

import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collector.Characteristics.CONCURRENT;
import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;
import static java.util.stream.Collector.Characteristics.UNORDERED;

/**
 * Helper class to succinctly aggregate and derive various approximate statistical measures.
 */
public class Approximators
{
    /**
     * Get default approximate cardinality (unique) aggregator.
     *
     * @param <T> Type of item to aggregate
     * @return
     */
    public static <T> Cardinality<T> cardinality()
    {
        return CardinalityImpl.create();
    }

    /**
     * Calculate number of unique items in a stream.
     *
     * @param stream
     * @param <T>
     * @return
     */
    public static <T> Long cardinality(Stream<T> stream)
    {
        return stream.collect(cardinalityCollector());
    }

    /**
     * Used to collect a stream into a cardinality counter, return estimated number of unique items (cardinality).
     *
     * @param <T> Type of item to aggregate
     * @return Number of unique elements in the stream.
     */
    public static <T> Collector<T, Cardinality<T>, Long> cardinalityCollector()
    {
        return Collector.of(
                Approximators::cardinality,
                Cardinality::accept,
                Cardinality::mergeWith,
                Cardinality::get,
                UNORDERED, CONCURRENT
        );
    }

    /**
     * Get default item frequency aggregator.
     *
     * @param <T> Type of item to count.
     * @return
     */
    public static <T> Frequency<T> frequency()
    {
        return HashedFrequencyImpl.create();
    }

    /**
     * Collect item frequency information over a stream.
     *
     * @param stream
     * @param <T>
     * @return
     */
    public static <T> Frequency<T> frequency(Stream<T> stream)
    {
        return stream.collect(frequencyCollector());
    }

    /**
     * Aggregate into a frequency counter, returning the counters for inspection.
     *
     * @param <T> Type of item to count.
     * @return Populated frequency counters.
     */
    public static <T> Collector<T, Frequency<T>, Frequency<T>> frequencyCollector()
    {
        return Collector.of(
                Approximators::frequency,
                Frequency::accept,
                Frequency::mergeWith,
                IDENTITY_FINISH, UNORDERED, CONCURRENT
        );
    }

    /**
     * Get default numeric item distribution (percentile) aggregator.
     *
     * @return
     */
    public static NumericDistribution distribution()
    {
        return NumericDistributionImpl.create();
    }

    /*
     * Due to wonkiness of Java primitive vs Number types we have to have multiple variants
     * There's no "stream friendly" equivalent of Number.
     */

    public static NumericDistribution distribution(IntStream intStream)
    {
        return intStream.collect(
                Approximators::distribution,
                NumericDistribution::accept,
                NumericDistribution::mergeWith
        );
    }

    public static NumericDistribution distribution(LongStream longStream)
    {
        return longStream.collect(
                Approximators::distribution,
                NumericDistribution::accept,
                NumericDistribution::mergeWith
        );
    }

    public static NumericDistribution distribution(DoubleStream doubleStream)
    {
        return doubleStream.collect(
                Approximators::distribution,
                NumericDistribution::accept,
                NumericDistribution::mergeWith
        );
    }

    /**
     * Aggregate into a numberic distribution tracker, returning the distribution data for inspection.
     *
     * @return Populated distribution data
     */
    public static Collector<Integer, NumericDistribution, NumericDistribution> intDistributionCollector()
    {
        return Collector.of(
                Approximators::distribution,
                NumericDistribution::accept,
                NumericDistribution::mergeWith,
                IDENTITY_FINISH, UNORDERED, CONCURRENT
        );
    }

    public static Collector<Long, NumericDistribution, NumericDistribution> longDistributionCollector()
    {
        return Collector.of(
                Approximators::distribution,
                NumericDistribution::accept,
                NumericDistribution::mergeWith,
                IDENTITY_FINISH, UNORDERED, CONCURRENT
        );
    }

    public static Collector<Double, NumericDistribution, NumericDistribution> doubleDistributionCollector()
    {
        return Collector.of(
                Approximators::distribution,
                NumericDistribution::accept,
                NumericDistribution::mergeWith,
                IDENTITY_FINISH, UNORDERED, CONCURRENT
        );
    }
}
