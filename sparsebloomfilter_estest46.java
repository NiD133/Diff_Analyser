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

public class SparseBloomFilter_ESTestTest46 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test45() throws Throwable {
        Shape shape0 = Shape.fromKM(994, 994);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        byte[] byteArray0 = new byte[4];
        EnhancedDoubleHasher enhancedDoubleHasher0 = new EnhancedDoubleHasher(byteArray0);
        sparseBloomFilter0.merge((Hasher) enhancedDoubleHasher0);
        Shape shape1 = Shape.fromNM(1, 408);
        SparseBloomFilter sparseBloomFilter1 = new SparseBloomFilter(shape1);
        // Undeclared exception!
        try {
            sparseBloomFilter1.merge((BloomFilter<?>) sparseBloomFilter0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Value in list 990 is greater than maximum value (407)
            //
            verifyException("org.apache.commons.collections4.bloomfilter.SparseBloomFilter", e);
        }
    }
}
