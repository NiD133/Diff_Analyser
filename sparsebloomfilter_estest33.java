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

public class SparseBloomFilter_ESTestTest33 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        Shape shape0 = Shape.fromNM(64, 64);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        boolean boolean0 = sparseBloomFilter0.merge((IndexExtractor) sparseBloomFilter0);
        assertTrue(boolean0);
    }
}
