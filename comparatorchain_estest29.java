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

public class ComparatorChain_ESTestTest29 extends ComparatorChain_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        LinkedList<Comparator<Object>> linkedList0 = new LinkedList<Comparator<Object>>();
        BitSet bitSet0 = new BitSet();
        ComparatorChain<Object> comparatorChain0 = new ComparatorChain<Object>(linkedList0, bitSet0);
        boolean boolean0 = comparatorChain0.equals("ComparatorChains must contain at least one Comparator");
        assertFalse(boolean0);
    }
}