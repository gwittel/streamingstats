package software.operable.streamingstats.aggregators.approximate;

import org.testng.annotations.Test;
import software.operable.streamingstats.aggregators.LongFrequency;

import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

public class TestApproximateLongFrequency
{
    @Test(expectedExceptions = NullPointerException.class)
    public void testMergeWithNull()
            throws Exception
    {
        ApproximateLongFrequency.create().mergeWith(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMergeWithIncompatibleImplementation()
            throws Exception
    {
        ApproximateLongFrequency.create().mergeWith(dummyHashedFrequency());
    }

    @Test
    public void testMergeWith()
            throws Exception
    {
        LongStream ones = LongStream.generate(() -> 1);
        LongStream twos = LongStream.generate(() -> 2);

        ApproximateLongFrequency freq1 = ApproximateLongFrequency.create();
        freq1.addAll(ones.limit(1000));

        ApproximateLongFrequency freq2 = ApproximateLongFrequency.create();
        freq2.addAll(twos.limit(5000));

        freq1.mergeWith(freq2);

        assertThat(freq1.get(1)).isCloseTo(1000, withinPercentage(1.0));
        assertThat(freq1.get(2)).isCloseTo(5000, withinPercentage(1.0));
    }

    private HashedLongFrequency dummyHashedFrequency()
    {
        return new DummyHashedFrequency();
    }

    private static class DummyHashedFrequency
            implements HashedLongFrequency
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
        public LongFrequency mergeWith(LongFrequency other)
        {
            return this;
        }
    }
}