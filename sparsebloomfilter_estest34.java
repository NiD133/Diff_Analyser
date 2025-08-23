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

public class SparseBloomFilter_ESTestTest34 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        Shape shape0 = Shape.fromKM(2147483639, 2147483639);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        long[] longArray0 = new long[2];
        LongBiPredicate longBiPredicate0 = mock(LongBiPredicate.class, new ViolatedAssumptionAnswer());
        doReturn(true, false, true).when(longBiPredicate0).test(anyLong(), anyLong());
        CountingLongPredicate countingLongPredicate0 = new CountingLongPredicate(longArray0, longBiPredicate0);
        countingLongPredicate0.test(2147483639L);
        LongPredicate longPredicate0 = countingLongPredicate0.negate();
        boolean boolean0 = sparseBloomFilter0.processBitMaps(longPredicate0);
        assertFalse(boolean0);
    }
}
