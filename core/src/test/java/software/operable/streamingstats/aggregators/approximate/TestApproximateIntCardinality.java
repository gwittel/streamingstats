package software.operable.streamingstats.aggregators.approximate;

import org.testng.annotations.Test;
import software.operable.streamingstats.aggregators.IntCardinality;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

public class TestApproximateIntCardinality
{
    @Test(expectedExceptions = NullPointerException.class)
    public void testMergeWithNull()
            throws Exception
    {
        ApproximateIntCardinality.create().mergeWith(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMergeWithIncompatibleImplementation()
            throws Exception
    {
        ApproximateIntCardinality.create().mergeWith(dummyIntCardinality());
    }

    @Test
    public void testMergeWith()
            throws Exception
    {
        // This feels more like we're testing the cardinality implementation vs the merging
        ApproximateIntCardinality card1 = ApproximateIntCardinality.create();
        IntStream.rangeClosed(1, 100000).forEach(card1::accept);

        ApproximateIntCardinality card2 = ApproximateIntCardinality.create();
        IntStream.rangeClosed(100001, 200001).forEach(card2::accept);

        assertThat(card1.get()).isCloseTo(card2.get(), withinPercentage(2.5));

        long oldCardinality = card1.get();
        card1.mergeWith(card2);

        assertThat(card1.get()).isCloseTo(2 * oldCardinality, withinPercentage(1));
    }

    private IntCardinality dummyIntCardinality()
    {
        return new DummyCardinality();
    }

    private static class DummyCardinality
            implements IntCardinality
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
        public IntCardinality mergeWith(IntCardinality other)
        {
            return this;
        }

        @Override
        public void accept(int s)
        {
            return;
        }
    }
}