package software.operable.streamingstats.aggregators.approximate;

import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import software.operable.streamingstats.aggregators.Cardinality;
import software.operable.streamingstats.aggregators.Frequency;
import software.operable.streamingstats.aggregators.NumericDistribution;
import software.operable.streamingstats.aggregators.NumericSummary;
import software.operable.streamingstats.aggregators.Summary;

import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collector.Characteristics.CONCURRENT;
import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;
import static java.util.stream.Collector.Characteristics.UNORDERED;

/**
 * Factory class to succinctly create aggregators and derive various approximate statistical measures.
 */
@Beta
public class Approximators
{
    /**
     * Get default approximate cardinality (unique) aggregator.
     *
     * @param <T> Type of item to aggregate
     */
    public static <T> Cardinality<T> cardinality()
    {
        return ApproximateCardinality.create();
    }

    /**
     * Calculate number of unique items in a stream.
     */
    public static <T> Long cardinalityOf(Stream<T> stream)
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
     */
    public static <T> Frequency<T> frequency()
    {
        return ApproximateFrequency.create();
    }

    /**
     * Collect item frequency information over a stream.
     */
    public static <T> Frequency<T> frequencyOf(Stream<T> stream)
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
     * Get the default approximate summary aggregator
     *
     * @param <T> Type of item to track.
     * @return Summary aggregator
     */
    public static <T> Summary<T> summary()
    {
        return ApproximateSummary.create();
    }

    /**
     * Get a summary of the supplied finite stream.
     */
    public static <T> Summary<T> summaryOf(Stream<T> stream)
    {
        return stream.collect(summaryCollector());
    }

    public static <T> Collector<T, Summary<T>, Summary<T>> summaryCollector()
    {
        return Collector.of(
                Approximators::summary,
                Summary::accept,
                Summary::mergeWith,
                IDENTITY_FINISH, UNORDERED, CONCURRENT
        );
    }

    /**
     * Get default numeric item distribution (percentile) aggregator.
     */
    public static NumericDistribution distribution()
    {
        return ApproximateDistribution.create();
    }

    /*
     * Due to wonkiness of Java primitive vs Number types we have to have multiple variants
     * There's no "stream friendly" equivalent of Number.
     */

    public static NumericDistribution distributionOf(IntStream intStream)
    {
        return intStream.collect(
                Approximators::distribution,
                NumericDistribution::accept,
                NumericDistribution::mergeWith
        );
    }

    public static NumericDistribution distributionOf(LongStream longStream)
    {
        return longStream.collect(
                Approximators::distribution,
                NumericDistribution::accept,
                NumericDistribution::mergeWith
        );
    }

    public static NumericDistribution distributionOf(DoubleStream doubleStream)
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

    public static NumericSummary numericSummary()
    {
        return ApproximateNumericSummary.create();
    }

    /**
     * Get a summary of the supplied finite int stream.
     */
    public static NumericSummary numericSummaryOf(IntStream stream)
    {
        return stream.collect(
                Approximators::numericSummary,
                NumericSummaryConsumer.get(),
                NumericSummary::mergeWith
        );
    }

    /**
     * Get a summary of the supplied finite long stream.
     */
    public static NumericSummary numericSummaryOf(LongStream stream)
    {
        return stream.collect(
                Approximators::numericSummary,
                NumericSummaryConsumer.get(),
                NumericSummary::mergeWith
        );
    }

    /**
     * Get a summary of the supplied finite double stream.
     */
    public static NumericSummary numericSummaryOf(DoubleStream stream)
    {
        return stream.collect(
                Approximators::numericSummary,
                NumericSummaryConsumer.get(),
                NumericSummary::mergeWith
        );
    }

    /**
     * Get the default internal HLL Implementation
     */
    @VisibleForTesting
    static HyperLogLogPlus defaultHll()
    {
        return new HyperLogLogPlus(14, 32);
    }
}
