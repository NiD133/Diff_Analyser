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

public class ComparatorChain_ESTestTest21 extends ComparatorChain_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        BitSet bitSet0 = new BitSet();
        LinkedList<Comparator<Integer>> linkedList0 = new LinkedList<Comparator<Integer>>();
        ComparatorChain<Integer> comparatorChain0 = new ComparatorChain<Integer>(linkedList0, bitSet0);
        comparatorChain0.addComparator((Comparator<Integer>) comparatorChain0, false);
        comparatorChain0.size();
        assertEquals("{}", bitSet0.toString());
        assertEquals(0, bitSet0.cardinality());
    }
}
