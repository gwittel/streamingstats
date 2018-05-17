package software.operable.streamingstats.aggregators.approximate;

import software.operable.streamingstats.aggregators.NumericSummary;

import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;

/**
 * Helper class for applying stream operations to {@link NumericSummary}.
 */
public class NumericSummaryConsumer implements ObjIntConsumer<NumericSummary>,
        ObjLongConsumer<NumericSummary>, ObjDoubleConsumer<NumericSummary>
{
    private static final NumericSummaryConsumer consumer = new NumericSummaryConsumer();

    NumericSummaryConsumer()
    {
    }

    public static NumericSummaryConsumer get()
    {
        return consumer;
    }

    @Override
    public void accept(NumericSummary numericSummary, int value)
    {
        numericSummary.accept(value);
    }

    @Override
    public void accept(NumericSummary numericSummary, double value)
    {
        numericSummary.accept(value);
    }

    @Override
    public void accept(NumericSummary numericSummary, long value)
    {
        numericSummary.accept(value);
    }
}
