package software.operable.streamingstats;

import com.google.common.annotations.Beta;

/**
 * Generic number aggregator
 */
@Beta
public interface NumericAggregator
        extends IntAggregator, LongAggregator, DoubleAggregator
{
}
