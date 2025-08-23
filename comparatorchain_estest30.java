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

public class ComparatorChain_ESTestTest30 extends ComparatorChain_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        ToIntFunction<Integer> toIntFunction0 = (ToIntFunction<Integer>) mock(ToIntFunction.class, new ViolatedAssumptionAnswer());
        doReturn(780, (-1576)).when(toIntFunction0).applyAsInt(anyInt());
        Comparator<Integer> comparator0 = Comparator.comparingInt((ToIntFunction<? super Integer>) toIntFunction0);
        ComparatorChain<Integer> comparatorChain0 = new ComparatorChain<Integer>(comparator0, true);
        Integer integer0 = new Integer(780);
        comparatorChain0.compare(integer0, integer0);
        // Undeclared exception!
        try {
            comparatorChain0.setComparator(780, comparator0, false);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //
            // Comparator ordering cannot be changed after the first comparison is performed
            //
            verifyException("org.apache.commons.collections4.comparators.ComparatorChain", e);
        }
    }
}
