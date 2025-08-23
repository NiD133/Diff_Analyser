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

public class SparseBloomFilter_ESTestTest32 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        Shape shape0 = Shape.fromNMK(12, 12, 12);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        int[] intArray0 = new int[1];
        intArray0[0] = 12;
        IndexExtractor indexExtractor0 = IndexExtractor.fromIndexArray(intArray0);
        // Undeclared exception!
        try {
            sparseBloomFilter0.merge(indexExtractor0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Value in list 12 is greater than maximum value (11)
            //
            verifyException("org.apache.commons.collections4.bloomfilter.SparseBloomFilter", e);
        }
    }
}
