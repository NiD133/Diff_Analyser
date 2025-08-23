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

public class SparseBloomFilter_ESTestTest40 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test39() throws Throwable {
        Shape shape0 = Shape.fromNM(1618, 1618);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        EnhancedDoubleHasher enhancedDoubleHasher0 = new EnhancedDoubleHasher(1618, 1618);
        sparseBloomFilter0.merge((Hasher) enhancedDoubleHasher0);
        long[] longArray0 = sparseBloomFilter0.asBitMapArray();
        assertEquals(26, longArray0.length);
    }
}
