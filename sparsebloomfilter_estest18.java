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

public class SparseBloomFilter_ESTestTest18 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        Shape shape0 = Shape.fromKM(3130, 1);
        EnhancedDoubleHasher enhancedDoubleHasher0 = new EnhancedDoubleHasher(3130, 3130);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        // Undeclared exception!
        try {
            sparseBloomFilter0.merge((Hasher) enhancedDoubleHasher0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Value in list 2146453540 is greater than maximum value (0)
            //
            verifyException("org.apache.commons.collections4.bloomfilter.SparseBloomFilter", e);
        }
    }
}
