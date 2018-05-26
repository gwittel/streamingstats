package software.operable.streamingstats;

import com.google.common.annotations.Beta;

import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/**
 * Generic int aggregator.
 * <p>
 * Type support is currently limited by the currently available primitive friendly consumers.
 */
@Beta
public interface IntAggregator
        extends IntConsumer
{
    default void add(int item)
    {
        accept(item);
    }

    default void addAll(int... values)
    {
        for (int v : values) {
            accept(v);
        }
    }

    default void addAll(IntStream intStream)
    {
        intStream.forEach(this::accept);
    }
}
