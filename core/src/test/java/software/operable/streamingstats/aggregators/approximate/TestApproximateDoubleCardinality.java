package software.operable.streamingstats.aggregators.approximate;

import org.testng.annotations.Test;
import software.operable.streamingstats.aggregators.DoubleCardinality;

import java.util.stream.DoubleStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

public class TestApproximateDoubleCardinality
{
    @Test(expectedExceptions = NullPointerException.class)
    public void testMergeWithNull()
            throws Exception
    {
        ApproximateDoubleCardinality.create().mergeWith(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMergeWithIncompatibleImplementation()
            throws Exception
    {
        ApproximateDoubleCardinality.create().mergeWith(dummyDoubleCardinality());
    }

    @Test
    public void testMergeWith()
            throws Exception
    {

        // This feels more like we're testing the cardinality implementation vs the merging
        ApproximateDoubleCardinality card1 = ApproximateDoubleCardinality.create();
        DoubleStream.iterate(1, s -> s + 1.0).limit(100_000).forEach(card1::accept);

        ApproximateDoubleCardinality card2 = ApproximateDoubleCardinality.create();
        DoubleStream.iterate(100_001, s -> s + 1.0).limit(100_000).forEach(card2::accept);

        assertThat(card1.get()).isCloseTo(card2.get(), withinPercentage(2.5));

        long oldCardinality = card1.get();
        card1.mergeWith(card2);

        assertThat(card1.get()).isCloseTo(2 * oldCardinality, withinPercentage(1));
    }

    private DoubleCardinality dummyDoubleCardinality()
    {
        return new DummyCardinality();
    }

    private static class DummyCardinality
            implements DoubleCardinality
    {
        DummyCardinality()
        {
        }

        @Override
        public long get()
        {
            return 0;
        }

        @Override
        public DoubleCardinality mergeWith(DoubleCardinality other)
        {
            return this;
        }

        @Override
        public void accept(double s)
        {
            return;
        }
    }
}