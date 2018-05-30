package software.operable.streamingstats.aggregators.approximate;

import org.testng.annotations.Test;
import software.operable.streamingstats.aggregators.NumericDistribution;

import static org.testng.Assert.assertEquals;

public class TestApproximateDistribution
{
    @Test(expectedExceptions = NullPointerException.class)
    public void testMergeWithNull()
            throws Exception
    {
        ApproximateDistribution.create().mergeWith(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMergeWithIncompatibleImplementation()
            throws Exception
    {
        ApproximateDistribution.create()
                .mergeWith(dummyNumericDistribution());
    }

    @Test
    public void testMergeWith()
            throws Exception
    {
        // Other than making sure merge works, is there anything really useful here?

        NumericDistribution dist1 = ApproximateDistribution.create();
        dist1.addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        NumericDistribution dist2 = ApproximateDistribution.create();
        dist2.addAll(11, 12, 13, 14, 15, 16, 17, 18, 19, 20);

        assertEquals(dist1.quantile(0.5), 5, 1);

        dist1.mergeWith(dist2);

        assertEquals(dist1.quantile(0.5), 10, 1);
    }

    @Test
    public void testCdf()
            throws Exception
    {
    }

    private NumericDistribution dummyNumericDistribution()
    {
        return new DummyNumericDistribution();
    }

    private static class DummyNumericDistribution
            implements
            NumericDistribution
    {

        @Override
        public double cdf(double cutoff)
        {
            return 0;
        }

        @Override
        public double quantile(double quantile)
        {
            return 0;
        }

        @Override
        public NumericDistribution mergeWith(NumericDistribution other)
        {
            return this;
        }

        @Override
        public void accept(double value)
        {

        }

        @Override
        public void accept(int value)
        {

        }

        @Override
        public void accept(long value)
        {

        }
    }

    @Test
    public void testQuantile()
            throws Exception
    {
    }
}