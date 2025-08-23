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

public class ComparatorChain_ESTestTest9 extends ComparatorChain_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        long[] longArray0 = new long[0];
        BitSet bitSet0 = BitSet.valueOf(longArray0);
        ComparatorChain<ComparatorChain<Object>> comparatorChain0 = new ComparatorChain<ComparatorChain<Object>>((List<Comparator<ComparatorChain<Object>>>) null, bitSet0);
        // Undeclared exception!
        try {
            comparatorChain0.setComparator(2147483645, (Comparator<ComparatorChain<Object>>) comparatorChain0, true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.collections4.comparators.ComparatorChain", e);
        }
    }
}
