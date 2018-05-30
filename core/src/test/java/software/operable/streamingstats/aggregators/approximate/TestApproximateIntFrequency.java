package software.operable.streamingstats.aggregators.approximate;

import org.testng.annotations.Test;
import software.operable.streamingstats.aggregators.IntFrequency;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

public class TestApproximateIntFrequency
{
    @Test(expectedExceptions = NullPointerException.class)
    public void testMergeWithNull()
            throws Exception
    {
        ApproximateIntFrequency.create().mergeWith(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMergeWithIncompatibleImplementation()
            throws Exception
    {
        ApproximateIntFrequency.create().mergeWith(dummyHashedFrequency());
    }

    @Test
    public void testMergeWith()
            throws Exception
    {
        IntStream ones = IntStream.generate(() -> 1);
        IntStream twos = IntStream.generate(() -> 2);

        ApproximateIntFrequency freq1 = ApproximateIntFrequency.create();
        freq1.addAll(ones.limit(1000));

        ApproximateIntFrequency freq2 = ApproximateIntFrequency.create();
        freq2.addAll(twos.limit(5000));

        freq1.mergeWith(freq2);

        assertThat(freq1.get(1)).isCloseTo(1000, withinPercentage(1.0));
        assertThat(freq1.get(2)).isCloseTo(5000, withinPercentage(1.0));
    }

    private HashedIntFrequency dummyHashedFrequency()
    {
        return new DummyHashedFrequency();
    }

    private static class DummyHashedFrequency
            implements HashedIntFrequency
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
        public IntFrequency mergeWith(IntFrequency other)
        {
            return this;
        }
    }
}