package software.operable.streamingstats.aggregators.approximate;

import org.testng.annotations.Test;
import software.operable.streamingstats.aggregators.Summary;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;
import static org.testng.Assert.assertEquals;

public class TestApproximateSummary
{
    @Test(expectedExceptions = NullPointerException.class)
    public void testMergeWithNull()
            throws Exception
    {
        ApproximateSummary.<String>create().mergeWith(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMergeWithIncompatibleImplementation()
            throws Exception
    {
        ApproximateSummary.<String>create().mergeWith(dummySummary());
    }

    @Test
    public void testMergeWith()
            throws Exception
    {
        Stream<Integer> ones = Stream.generate(() -> -1);
        Stream<Integer> twos = Stream.generate(() -> -2);

        // This feels more like we're testing the underlying implementation vs the merging
        ApproximateSummary<Integer> summary1 = ApproximateSummary.create();
        IntStream.range(0, 100000).forEach(summary1::accept);
        summary1.addAll(ones.limit(1000));

        ApproximateSummary<Integer> summary2 = ApproximateSummary.create();
        IntStream.range(100000, 200000).forEach(summary2::accept);
        summary2.addAll(twos.limit(1000));

        // Size Checks
        assertEquals(summary1.size(), summary2.size());

        // Card checks
        assertThat(summary1.cardinality()).isCloseTo(summary2.cardinality(), withinPercentage(2));

        // Freq checks
        assertThat(summary1.frequency(-1)).isCloseTo(1000, withinPercentage(1.0));
        assertThat(summary2.frequency(-2)).isCloseTo(1000, withinPercentage(1.0));

        // Merging
        long oldCardinality = summary1.cardinality();
        long oldSize = summary1.size();

        summary1.mergeWith(summary2);

        assertEquals(summary1.size(), oldSize * 2);
        assertThat(summary1.cardinality()).isCloseTo(2 * oldCardinality, withinPercentage(1));
        assertThat(summary1.frequency(-2)).isCloseTo(1000, withinPercentage(1.0));
    }

    private <T> Summary<T> dummySummary()
    {
        return new TestApproximateSummary.DummySummary<>();
    }

    private static class DummySummary<T>
            implements Summary<T>
    {
        DummySummary()
        {
        }

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
        public long frequency(T value)
        {
            return 0;
        }

        @Override
        public Summary<T> mergeWith(Summary<T> other)
        {
            return this;
        }

        @Override
        public void accept(T s)
        {
            return;
        }
    }
}