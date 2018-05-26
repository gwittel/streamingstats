package software.operable.streamingstats;

import com.google.common.annotations.Beta;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Baseline aggregator.
 * <p>
 * Note that nullability is not explicitly required.  It is currently up
 * to the specific implementation.  This might change in the near future.
 *
 * @param <T> Type of item to track.
 */
@Beta
public interface Aggregator<T>
        extends Consumer<T>
{
    /**
     * Add another item into this aggregator.
     * <p>
     * Alias for {@code accept}
     */
    default void add(T item)
    {
        accept(item);
    }

    /**
     * Add multiple values into this aggregator.
     */
    default void addAll(Collection<T> values)
    {
        for (T v : values) {
            add(v);
        }
    }

    /**
     * Add multiple values into this aggregator.
     */
    default void addAll(T... values)
    {
        for (T v : values) {
            add(v);
        }
    }

    /**
     * Consume stream into aggregator
     */
    default void addAll(Stream<T> stream)
    {
        stream.forEach(this::accept);
    }
}
