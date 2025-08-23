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

public class SimpleBloomFilter_ESTestTest43 extends SimpleBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test42() throws Throwable {
        Shape shape0 = Shape.fromNMK(19, 19, 19);
        SimpleBloomFilter simpleBloomFilter0 = new SimpleBloomFilter(shape0);
        boolean boolean0 = simpleBloomFilter0.merge((BloomFilter<?>) simpleBloomFilter0);
        boolean boolean1 = simpleBloomFilter0.isEmpty();
        assertEquals(0, simpleBloomFilter0.characteristics());
        assertTrue(boolean1 == boolean0);
        assertTrue(boolean1);
    }
}
