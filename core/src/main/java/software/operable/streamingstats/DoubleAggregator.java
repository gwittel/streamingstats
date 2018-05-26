package software.operable.streamingstats;

import com.google.common.annotations.Beta;

import java.util.function.DoubleConsumer;
import java.util.stream.DoubleStream;

/**
 * Generic double aggregator.
 * <p>
 * Type support is currently limited by the currently available primitive friendly consumers.
 */
@Beta
public interface DoubleAggregator
        extends DoubleConsumer
{
    default void add(double item)
    {
        accept(item);
    }

    default void addAll(double... values)
    {
        for (double v : values) {
            accept(v);
        }
    }

    default void addAll(DoubleStream doubleStream)
    {
        doubleStream.forEach(this::accept);
    }
}
