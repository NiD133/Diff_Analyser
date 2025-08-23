package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.BitSet;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.functors.ClosureTransformer;
import org.apache.commons.collections4.functors.ComparatorPredicate;
import org.apache.commons.collections4.functors.ExceptionClosure;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class ComparatorChain_ESTestTest4 extends ComparatorChain_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        ToLongFunction<Object> toLongFunction0 = (ToLongFunction<Object>) mock(ToLongFunction.class, new ViolatedAssumptionAnswer());
        doReturn(513L, (-971L)).when(toLongFunction0).applyAsLong(any());
        Comparator<Object> comparator0 = Comparator.comparingLong((ToLongFunction<? super Object>) toLongFunction0);
        ComparatorChain<Object> comparatorChain0 = new ComparatorChain<Object>(comparator0);
        LinkedList<Comparator<ComparatorChain<LongBuffer>>> linkedList0 = new LinkedList<Comparator<ComparatorChain<LongBuffer>>>();
        ComparatorChain<ComparatorChain<LongBuffer>> comparatorChain1 = new ComparatorChain<ComparatorChain<LongBuffer>>(linkedList0);
        Object object0 = new Object();
        comparatorChain0.compare(comparatorChain1, object0);
        boolean boolean0 = comparatorChain0.isLocked();
        assertTrue(boolean0);
    }
}
