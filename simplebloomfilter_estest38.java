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

public class SimpleBloomFilter_ESTestTest38 extends SimpleBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test37() throws Throwable {
        Shape shape0 = Shape.fromKM(28, 28);
        SimpleBloomFilter simpleBloomFilter0 = new SimpleBloomFilter(shape0);
        BitMapExtractor bitMapExtractor0 = BitMapExtractor.fromIndexExtractor(simpleBloomFilter0, (-1));
        LongBiPredicate longBiPredicate0 = mock(LongBiPredicate.class, new ViolatedAssumptionAnswer());
        doReturn(false).when(longBiPredicate0).test(anyLong(), anyLong());
        boolean boolean0 = simpleBloomFilter0.processBitMapPairs(bitMapExtractor0, longBiPredicate0);
        assertFalse(boolean0);
    }
}
