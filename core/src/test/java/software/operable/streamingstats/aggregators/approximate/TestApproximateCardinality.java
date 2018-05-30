package software.operable.streamingstats.aggregators.approximate;

import org.testng.annotations.Test;
import software.operable.streamingstats.aggregators.Cardinality;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

public class TestApproximateCardinality
{
    @Test(expectedExceptions = NullPointerException.class)
    public void testMergeWithNull()
            throws Exception
    {
        ApproximateCardinality.<String>create().mergeWith(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMergeWithIncompatibleImplementation()
            throws Exception
    {
        ApproximateCardinality.<String>create().mergeWith(dummyCardinality());
    }

    @Test
    public void testMergeWith()
            throws Exception
    {
        // This feels more like we're testing the cardinality implementation vs the merging
        ApproximateCardinality<Integer> card1 = ApproximateCardinality.create();
        IntStream.rangeClosed(1, 100000).forEach(card1::accept);

        ApproximateCardinality<Integer> card2 = ApproximateCardinality.create();
        IntStream.rangeClosed(100001, 200001).forEach(card2::accept);

        assertThat(card1.get()).isCloseTo(card2.get(), withinPercentage(2));

        long oldCardinality = card1.get();
        card1.mergeWith(card2);

        assertThat(card1.get()).isCloseTo(2 * oldCardinality, withinPercentage(1));
    }

    private <T> Cardinality<T> dummyCardinality()
    {
        return new DummyCardinality<>();
    }

    private static class DummyCardinality<T>
            implements Cardinality<T>
    {
        DummyCardinality() {}

        @Override
        public long get()
        {
            return 0;
        }

        @Override
        public Cardinality<T> mergeWith(Cardinality<T> other)
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