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

public class SparseBloomFilter_ESTestTest31 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        int[] intArray0 = new int[2];
        IndexExtractor indexExtractor0 = IndexExtractor.fromIndexArray(intArray0);
        Shape shape0 = Shape.fromNM(989, 2268);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        boolean boolean0 = sparseBloomFilter0.merge(indexExtractor0);
        assertTrue(boolean0);
    }
}
