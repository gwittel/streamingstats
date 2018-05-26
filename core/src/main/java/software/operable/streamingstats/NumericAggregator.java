package software.operable.streamingstats;

import com.google.common.annotations.Beta;

import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Generic number aggregator.
 * <p>
 * Type support is currently limited by the currently available primitive friendly consumers.
 */
@Beta
public interface NumericAggregator
        extends IntConsumer, LongConsumer, DoubleConsumer
{
    default void add(int item)
    {
        accept(item);
    }

    default void add(long item)
    {
        accept(item);
    }

    default void add(double item)
    {
        accept(item);
    }

    default void addAll(int... values)
    {
        for (int v : values) {
            accept(v);
        }
    }

    default void addAll(long... values)
    {
        for (long v : values) {
            accept(v);
        }
    }

    default void addAll(double... values)
    {
        for (double v : values) {
            accept(v);
        }
    }

    default void addAll(IntStream intStream)
    {
        intStream.forEach(this::accept);
    }

    default void addAll(LongStream longStream)
    {
        longStream.forEach(this::accept);
    }

    default void addAll(DoubleStream doubleStream)
    {
        doubleStream.forEach(this::accept);
    }
}
