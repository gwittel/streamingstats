package software.operable.streamingstats.aggregators.approximate;

import org.testng.annotations.Test;
import software.operable.streamingstats.aggregators.NumericSummary;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;
import static org.testng.Assert.assertEquals;

public class TestApproximateNumericSummary
{
    @Test(expectedExceptions = NullPointerException.class)
    public void testMergeWithNull()
            throws Exception
    {
        ApproximateNumericSummary.create().mergeWith(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMergeWithIncompatibleImplementation()
            throws Exception
    {
        ApproximateNumericSummary.create().mergeWith(dummySummary());
    }

    @Test
    public void testMergeWith()
            throws Exception
    {
        IntStream ones = IntStream.generate(() -> -1);
        IntStream twos = IntStream.generate(() -> -2);

        // This feels more like we're testing the underlying implementation vs the merging
        ApproximateNumericSummary summary1 = ApproximateNumericSummary.create();
        IntStream.range(0, 100000).forEach(summary1::accept);
        summary1.addAll(ones.limit(500));

        ApproximateNumericSummary summary2 = ApproximateNumericSummary.create();
        IntStream.range(100000, 200000).forEach(summary2::accept);
        summary2.addAll(twos.limit(500));

        // Size Checks
        assertEquals(summary1.size(), summary2.size());

        // Card checks
        assertThat(summary1.cardinality()).isCloseTo(summary2.cardinality(), withinPercentage(2));

        // Freq checks
        assertThat(summary1.frequency(-1)).isCloseTo(500, withinPercentage(1.0));
        assertThat(summary2.frequency(-2)).isCloseTo(500, withinPercentage(1.0));

        // Distribution checks.  Some wiggle room due to shift caused by frequency checks.
        assertThat(summary1.quantile(0.5)).isCloseTo(50_000, withinPercentage(1.0));
        assertThat(summary2.quantile(0.5)).isCloseTo(150_000, withinPercentage(1.0));

        // Merging
        long oldCardinality = summary1.cardinality();
        long oldSize = summary1.size();
        long oldFrequency = summary2.frequency(-2);

        summary1.mergeWith(summary2);

        assertEquals(summary1.size(), oldSize * 2);
        assertThat(summary1.cardinality()).isCloseTo(2 * oldCardinality, withinPercentage(1));
        assertThat(summary1.frequency(-2)).isCloseTo(oldFrequency, withinPercentage(1.0));
        assertThat(summary1.quantile(0.5)).isCloseTo(100_000, withinPercentage(1.0));
    }

    private NumericSummary dummySummary()
    {
        return new TestApproximateNumericSummary.DummySummary();
    }

    private static class DummySummary
            implements NumericSummary
    {

        @Override
        public long size()
        {
            return 0;
        }

        @Override
        public long cardinality()
        {
            return 0;
        }

        @Override
        public long frequency(double value)
        {
            return 0;
        }

        @Override
        public double cdf(double cutoff)
        {
            return 0.0;
        }

        @Override
        public double quantile(double quantile)
        {
            return 0.0;
        }

        @Override
        public NumericSummary mergeWith(NumericSummary other)
        {
            return this;
        }

        @Override
        public void accept(double value)
        {
            // noop
        }

        @Override
        public void accept(int value)
        {
            // noop
        }

        @Override
        public void accept(long value)
        {
            // noop
        }
    }
}