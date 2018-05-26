package software.operable.streamingstats.aggregators.approximate;

public interface BaseHashedFrequency
{
    /**
     * Add a pre-hashed value into the frequency aggregator data structure.
     *
     * @param hashedValue Value resulting from the implemented hash function.
     * @param count Amount to increment frequency by.
     */
    void addHashed(long hashedValue, long count);

    /**
     * Retrieve the frequency of a hashed value
     *
     * @param hashedValue Value resulting from the implemented hash function.
     * @return Frequency of the {@code hashedValue}
     */
    long getHashed(long hashedValue);
}
