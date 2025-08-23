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

public class SparseBloomFilter_ESTestTest16 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        Shape shape0 = Shape.fromNM(3, Integer.MAX_VALUE);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        EnhancedDoubleHasher enhancedDoubleHasher0 = new EnhancedDoubleHasher(3, 3);
        // Undeclared exception!
        sparseBloomFilter0.merge((Hasher) enhancedDoubleHasher0);
    }
}
