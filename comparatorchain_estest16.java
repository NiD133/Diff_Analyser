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

public class ComparatorChain_ESTestTest16 extends ComparatorChain_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        ComparatorChain<Object> comparatorChain0 = new ComparatorChain<Object>((Comparator<Object>) null, false);
        Object object0 = new Object();
        ComparatorChain<ComparatorChain<Integer>> comparatorChain1 = new ComparatorChain<ComparatorChain<Integer>>();
        // Undeclared exception!
        try {
            comparatorChain0.compare(object0, comparatorChain1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.collections4.comparators.ComparatorChain", e);
        }
    }
}
