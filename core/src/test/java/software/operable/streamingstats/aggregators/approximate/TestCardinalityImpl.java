/*
 * Copyright 2017 Greg Wittel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package software.operable.streamingstats.aggregators.approximate;

import org.testng.annotations.Test;
import software.operable.streamingstats.aggregators.Cardinality;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

public class TestCardinalityImpl
{
    @Test(expectedExceptions = NullPointerException.class)
    public void testMergeWithNull()
            throws Exception
    {
        CardinalityImpl.<String>create().mergeWith(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMergeWithIncompatibleImplementation()
            throws Exception
    {
        CardinalityImpl.<String>create().mergeWith(dummyCardinality());
    }

    @Test
    public void testMergeWith()
            throws Exception
    {
        // This feels more like we're testing the cardinality implementation vs the merging
        CardinalityImpl<Integer> card1 = CardinalityImpl.create();
        IntStream.rangeClosed(1, 100000).forEach(card1::accept);

        CardinalityImpl<Integer> card2 = CardinalityImpl.create();
        IntStream.rangeClosed(100001, 200001).forEach(card2::accept);

        assertThat(card1.get()).isCloseTo(card2.get(), withinPercentage(2));

        long oldCardinality = card1.get();
        card1.mergeWith(card2);

        assertThat(card1.get()).isCloseTo(2 * oldCardinality, withinPercentage(1));
    }

    private <T> Cardinality<T> dummyCardinality()
    {
        return new DummyCardinality<>();
    }

    private static class DummyCardinality<T>
            implements Cardinality<T>
    {
        DummyCardinality() {}

        @Override
        public long get()
        {
            return 0;
        }

        @Override
        public Cardinality<T> mergeWith(Cardinality<T> other)
        {
            return this;
        }

        @Override
        public void accept(T s)
        {
            return;
        }
    }
}