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

public class SparseBloomFilter_ESTestTest5 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        Shape shape0 = Shape.fromNM(64, 64);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        int[] intArray0 = new int[4];
        IndexExtractor indexExtractor0 = IndexExtractor.fromIndexArray(intArray0);
        boolean boolean0 = sparseBloomFilter0.contains(indexExtractor0);
        assertFalse(boolean0);
    }
}
