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

public class SparseBloomFilter_ESTestTest38 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test37() throws Throwable {
        Shape shape0 = Shape.fromKM(994, 994);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        int[] intArray0 = new int[4];
        intArray0[3] = (-1);
        IndexExtractor indexExtractor0 = IndexExtractor.fromIndexArray(intArray0);
        CellExtractor cellExtractor0 = CellExtractor.from(indexExtractor0);
        // Undeclared exception!
        try {
            sparseBloomFilter0.merge((IndexExtractor) cellExtractor0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Value in list -1 is less than 0
            //
            verifyException("org.apache.commons.collections4.bloomfilter.SparseBloomFilter", e);
        }
    }
}
