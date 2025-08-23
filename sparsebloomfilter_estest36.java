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

public class SparseBloomFilter_ESTestTest36 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test35() throws Throwable {
        Shape shape0 = Shape.fromKM(1045, 1045);
        EnhancedDoubleHasher enhancedDoubleHasher0 = new EnhancedDoubleHasher(997L, (-1591L));
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        boolean boolean0 = sparseBloomFilter0.merge((Hasher) enhancedDoubleHasher0);
        SimpleBloomFilter simpleBloomFilter0 = new SimpleBloomFilter(shape0);
        boolean boolean1 = simpleBloomFilter0.contains((BitMapExtractor) sparseBloomFilter0);
        assertFalse(boolean1 == boolean0);
        assertFalse(boolean1);
    }
}
