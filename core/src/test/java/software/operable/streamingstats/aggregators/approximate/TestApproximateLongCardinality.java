package software.operable.streamingstats.aggregators.approximate;

import org.testng.annotations.Test;
import software.operable.streamingstats.aggregators.LongCardinality;

import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

public class TestApproximateLongCardinality
{
    @Test(expectedExceptions = NullPointerException.class)
    public void testMergeWithNull()
            throws Exception
    {
        ApproximateLongCardinality.create().mergeWith(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMergeWithIncompatibleImplementation()
            throws Exception
    {
        ApproximateLongCardinality.create().mergeWith(dummyLongCardinality());
    }

    @Test
    public void testMergeWith()
            throws Exception
    {
        // This feels more like we're testing the cardinality implementation vs the merging
        ApproximateLongCardinality card1 = ApproximateLongCardinality.create();
        LongStream.rangeClosed(1, 100000).forEach(card1::accept);

        ApproximateLongCardinality card2 = ApproximateLongCardinality.create();
        LongStream.rangeClosed(100001, 200001).forEach(card2::accept);

        assertThat(card1.get()).isCloseTo(card2.get(), withinPercentage(2.5));

        long oldCardinality = card1.get();
        card1.mergeWith(card2);

        assertThat(card1.get()).isCloseTo(2 * oldCardinality, withinPercentage(1));
    }

    private LongCardinality dummyLongCardinality()
    {
        return new DummyCardinality();
    }

    private static class DummyCardinality
            implements LongCardinality
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
        public LongCardinality mergeWith(LongCardinality other)
        {
            return this;
        }

        @Override
        public void accept(long s)
        {
            return;
        }
    }
}