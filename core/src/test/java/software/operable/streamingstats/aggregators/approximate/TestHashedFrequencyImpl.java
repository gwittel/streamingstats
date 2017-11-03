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
import software.operable.streamingstats.aggregators.Frequency;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

public class TestHashedFrequencyImpl
{
    @Test(expectedExceptions = NullPointerException.class)
    public void testMergeWithNull()
            throws Exception
    {
        HashedFrequencyImpl.<String>create().mergeWith(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMergeWithIncompatibleImplementation()
            throws Exception
    {
        HashedFrequencyImpl.<String>create().mergeWith(dummyHashedFrequency());
    }

    @Test
    public void testMergeWith()
            throws Exception
    {
        Stream<Integer> ones = Stream.generate(() -> 1);
        Stream<Integer> twos = Stream.generate(() -> 2);

        HashedFrequencyImpl<Integer> freq1 = HashedFrequencyImpl.create();
        freq1.addAll(ones.limit(1000));

        HashedFrequencyImpl<Integer> freq2 = HashedFrequencyImpl.create();
        freq2.addAll(twos.limit(5000));

        freq1.mergeWith(freq2);

        assertThat(freq1.get(1)).isCloseTo(1000, withinPercentage(1.0));
        assertThat(freq1.get(2)).isCloseTo(5000, withinPercentage(1.0));
    }

    private <T> HashedFrequency<T> dummyHashedFrequency()
    {
        return new DummyHashedFrequency();
    }

    private static class DummyHashedFrequency<T>
            implements HashedFrequency<T>
    {

        @Override
        public void addHashed(long hashedValue, long count)
        {
            return;
        }

        @Override
        public long getHashed(long hashedValue)
        {
            return 0;
        }

        @Override
        public Frequency<T> mergeWith(Frequency<T> other)
        {
            return this;
        }
    }
}