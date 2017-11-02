/**
 * Copyright 2017 Greg Wittel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package software.operable.streamingstats;

import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

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
}
