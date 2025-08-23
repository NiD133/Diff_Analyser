package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class SparseBloomFilter_ESTestTest2 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        Shape shape0 = Shape.fromNM(22, 22);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        EnhancedDoubleHasher enhancedDoubleHasher0 = new EnhancedDoubleHasher((-1158L), (-613L));
        boolean boolean0 = sparseBloomFilter0.merge((Hasher) enhancedDoubleHasher0);
        assertTrue(boolean0);
        IndexFilter.ArrayTracker indexFilter_ArrayTracker0 = new IndexFilter.ArrayTracker(shape0);
        IntPredicate intPredicate0 = indexFilter_ArrayTracker0.negate();
        boolean boolean1 = sparseBloomFilter0.processIndices(intPredicate0);
        assertFalse(boolean1 == boolean0);
    }
}