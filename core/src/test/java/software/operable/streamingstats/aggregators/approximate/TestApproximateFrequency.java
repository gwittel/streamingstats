package software.operable.streamingstats.aggregators.approximate;

import org.testng.annotations.Test;
import software.operable.streamingstats.aggregators.Frequency;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

public class TestApproximateFrequency
{
    @Test(expectedExceptions = NullPointerException.class)
    public void testMergeWithNull()
            throws Exception
    {
        ApproximateFrequency.<String>create().mergeWith(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMergeWithIncompatibleImplementation()
            throws Exception
    {
        ApproximateFrequency.<String>create().mergeWith(dummyHashedFrequency());
    }

    @Test
    public void testMergeWith()
            throws Exception
    {
        Stream<Integer> ones = Stream.generate(() -> 1);
        Stream<Integer> twos = Stream.generate(() -> 2);

        ApproximateFrequency<Integer> freq1 = ApproximateFrequency.create();
        freq1.addAll(ones.limit(1000));

        ApproximateFrequency<Integer> freq2 = ApproximateFrequency.create();
        freq2.addAll(twos.limit(5000));

        freq1.mergeWith(freq2);

        assertThat(freq1.get(1)).isCloseTo(1000, withinPercentage(1.0));
        assertThat(freq1.get(2)).isCloseTo(5000, withinPercentage(1.0));
    }

    private <T> HashedFrequency<T> dummyHashedFrequency()
    {
        return new DummyHashedFrequency();
    }

    private static class DummyHashedFrequency<T>
            implements HashedFrequency<T>
    {

        @Override
        public void addHashed(long hashedValue, long count)
        {
            return;
        }

        @Override
        public long getHashed(long hashedValue)
        {
            return 0;
        }

        @Override
        public Frequency<T> mergeWith(Frequency<T> other)
        {
            return this;
        }
    }
}