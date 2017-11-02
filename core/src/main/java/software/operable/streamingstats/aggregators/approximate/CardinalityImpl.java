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
package software.operable.streamingstats.aggregators.approximate;

import com.clearspring.analytics.stream.cardinality.CardinalityMergeException;
import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import com.clearspring.analytics.stream.cardinality.ICardinality;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import software.operable.streamingstats.aggregators.Cardinality;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

public class CardinalityImpl<T>
        implements Cardinality<T>
{

    // TODO Consider alternate HLL - http://dsiutils.di.unimi.it/
    // Good for having large numbers of HLLs due to reduced object creation overhead
    private volatile ICardinality delegate;

    CardinalityImpl()
    {
        this.delegate = defaultHll();
    }

    public static <T> CardinalityImpl<T> create()
    {
        return new CardinalityImpl<>();
    }

    @Override
    public long get()
    {
        return delegate.cardinality();
    }

    @Override
    public Cardinality<T> mergeWith(Cardinality<T> other)
    {
        requireNonNull(other, "other is null");
        checkArgument(other instanceof CardinalityImpl, "Cannot merge Cardinality estimators of differing types");

        try {
            this.delegate = delegate.merge(((CardinalityImpl) other).delegate);
        }
        catch (CardinalityMergeException e) {
            throw new IllegalArgumentException("Problem merging estimators", e);
        }

        return this;
    }

    @Override
    public void accept(T t)
    {
        delegate.offer(t);
    }

    @VisibleForTesting
    HyperLogLogPlus defaultHll()
    {
        return new HyperLogLogPlus(14, 32);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("delegate", delegate)
                .toString();
    }
}
