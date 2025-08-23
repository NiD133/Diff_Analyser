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

public class ComparatorChain_ESTestTest20 extends ComparatorChain_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        ToIntFunction<Integer> toIntFunction0 = (ToIntFunction<Integer>) mock(ToIntFunction.class, new ViolatedAssumptionAnswer());
        doReturn(1, (-1576), (-1576), (-206)).when(toIntFunction0).applyAsInt(anyInt());
        Comparator<Integer> comparator0 = Comparator.comparingInt((ToIntFunction<? super Integer>) toIntFunction0);
        ComparatorChain<Integer> comparatorChain0 = new ComparatorChain<Integer>(comparator0, true);
        Integer integer0 = new Integer((-318));
        comparatorChain0.compare(integer0, integer0);
        int int0 = comparatorChain0.compare(integer0, integer0);
        assertTrue(comparatorChain0.isLocked());
        assertEquals(1, int0);
    }
}