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

public class ComparatorChain_ESTestTest12 extends ComparatorChain_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        Comparator<ComparatorChain<Object>> comparator0 = Comparator.nullsLast((Comparator<? super ComparatorChain<Object>>) null);
        ComparatorChain<ComparatorChain<Object>> comparatorChain0 = new ComparatorChain<ComparatorChain<Object>>(comparator0);
        LinkedList<Comparator<Object>> linkedList0 = new LinkedList<Comparator<Object>>();
        ComparatorChain<Object> comparatorChain1 = new ComparatorChain<Object>(linkedList0, (BitSet) null);
        comparatorChain0.compare(comparatorChain1, comparatorChain1);
        // Undeclared exception!
        try {
            comparatorChain0.setComparator((-67), comparator0);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //
            // Comparator ordering cannot be changed after the first comparison is performed
            //
            verifyException("org.apache.commons.collections4.comparators.ComparatorChain", e);
        }
    }
}
