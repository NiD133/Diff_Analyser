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

public class SparseBloomFilter_ESTestTest37 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test36() throws Throwable {
        Shape shape0 = Shape.fromKM(994, 994);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        byte[] byteArray0 = new byte[4];
        EnhancedDoubleHasher enhancedDoubleHasher0 = new EnhancedDoubleHasher(byteArray0);
        boolean boolean0 = sparseBloomFilter0.merge((Hasher) enhancedDoubleHasher0);
        boolean boolean1 = sparseBloomFilter0.merge((BitMapExtractor) sparseBloomFilter0);
        assertTrue(boolean1 == boolean0);
        assertTrue(boolean1);
    }
}
