package software.operable.streamingstats;

import com.google.common.annotations.Beta;

import java.util.function.LongConsumer;
import java.util.stream.LongStream;

/**
 * Generic long aggregator.
 * <p>
 * Type support is currently limited by the currently available primitive friendly consumers.
 */
@Beta
public interface LongAggregator
        extends LongConsumer
{
    default void add(long item)
    {
        accept(item);
    }

    default void addAll(long... values)
    {
        for (long v : values) {
            accept(v);
        }
    }

    default void addAll(LongStream longStream)
    {
        longStream.forEach(this::accept);
    }
}
