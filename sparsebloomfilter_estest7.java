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

public class SparseBloomFilter_ESTestTest7 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Shape shape0 = Shape.fromNMK(22, 22, 22);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        int int0 = sparseBloomFilter0.characteristics();
        assertEquals(1, int0);
    }
}