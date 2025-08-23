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

public class SparseBloomFilter_ESTestTest23 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        Shape shape0 = Shape.fromKM(3, 3);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        long[] longArray0 = new long[3];
        longArray0[2] = (long) 3;
        BitMapExtractor bitMapExtractor0 = BitMapExtractor.fromBitMapArray(longArray0);
        // Undeclared exception!
        try {
            sparseBloomFilter0.merge(bitMapExtractor0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Value in list 129 is greater than maximum value (2)
            //
            verifyException("org.apache.commons.collections4.bloomfilter.SparseBloomFilter", e);
        }
    }
}
