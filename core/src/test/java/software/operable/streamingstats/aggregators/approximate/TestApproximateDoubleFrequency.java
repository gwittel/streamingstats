package software.operable.streamingstats.aggregators.approximate;

import org.testng.annotations.Test;
import software.operable.streamingstats.aggregators.DoubleFrequency;

import java.util.stream.DoubleStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

public class TestApproximateDoubleFrequency
{
    @Test(expectedExceptions = NullPointerException.class)
    public void testMergeWithNull()
            throws Exception
    {
        ApproximateDoubleFrequency.create().mergeWith(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMergeWithIncompatibleImplementation()
            throws Exception
    {
        ApproximateDoubleFrequency.create().mergeWith(dummyHashedFrequency());
    }

    @Test
    public void testMergeWith()
            throws Exception
    {
        DoubleStream ones = DoubleStream.generate(() -> 1);
        DoubleStream twos = DoubleStream.generate(() -> 2);

        ApproximateDoubleFrequency freq1 = ApproximateDoubleFrequency.create();
        freq1.addAll(ones.limit(1000));

        ApproximateDoubleFrequency freq2 = ApproximateDoubleFrequency.create();
        freq2.addAll(twos.limit(5000));

        freq1.mergeWith(freq2);

        assertThat(freq1.get(1)).isCloseTo(1000, withinPercentage(1.0));
        assertThat(freq1.get(2)).isCloseTo(5000, withinPercentage(1.0));
    }

    private HashedDoubleFrequency dummyHashedFrequency()
    {
        return new DummyHashedFrequency();
    }

    private static class DummyHashedFrequency
            implements HashedDoubleFrequency
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
        public DoubleFrequency mergeWith(DoubleFrequency other)
        {
            return this;
        }
    }
}