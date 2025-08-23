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

public class SimpleBloomFilter_ESTestTest7 extends SimpleBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Shape shape0 = Shape.fromNMK(3419, 3419, 1);
        SimpleBloomFilter simpleBloomFilter0 = new SimpleBloomFilter(shape0);
        long[] longArray0 = new long[3];
        LongBiPredicate longBiPredicate0 = mock(LongBiPredicate.class, new ViolatedAssumptionAnswer());
        doReturn(false, false, false, false, false).when(longBiPredicate0).test(anyLong(), anyLong());
        CountingLongPredicate countingLongPredicate0 = new CountingLongPredicate(longArray0, longBiPredicate0);
        LongPredicate longPredicate0 = countingLongPredicate0.negate();
        boolean boolean0 = simpleBloomFilter0.processBitMaps(longPredicate0);
        assertTrue(boolean0);
    }
}
