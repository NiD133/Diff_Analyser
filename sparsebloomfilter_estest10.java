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

public class SparseBloomFilter_ESTestTest10 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        Shape shape0 = Shape.fromKM(989, 989);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        EnhancedDoubleHasher enhancedDoubleHasher0 = new EnhancedDoubleHasher(989, 989);
        sparseBloomFilter0.merge((Hasher) enhancedDoubleHasher0);
        IndexFilter.ArrayTracker indexFilter_ArrayTracker0 = new IndexFilter.ArrayTracker(shape0);
        // Undeclared exception!
        sparseBloomFilter0.processIndices(indexFilter_ArrayTracker0);
    }
}
