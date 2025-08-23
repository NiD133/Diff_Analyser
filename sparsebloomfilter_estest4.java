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

public class SparseBloomFilter_ESTestTest4 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        Shape shape0 = Shape.fromNMK(22, 22, 22);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        Shape shape1 = sparseBloomFilter0.getShape();
        assertEquals(0.6931471805599453, shape1.estimateMaxN(), 0.01);
    }
}
