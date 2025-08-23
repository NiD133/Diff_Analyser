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

public class SimpleBloomFilter_ESTestTest3 extends SimpleBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Shape shape0 = Shape.fromNMK(78, 78, 2);
        SimpleBloomFilter simpleBloomFilter0 = new SimpleBloomFilter(shape0);
        EnhancedDoubleHasher enhancedDoubleHasher0 = new EnhancedDoubleHasher(1L, (-9223372036854775808L));
        boolean boolean0 = simpleBloomFilter0.merge((Hasher) enhancedDoubleHasher0);
        assertTrue(boolean0);
        boolean boolean1 = simpleBloomFilter0.merge((BitMapExtractor) simpleBloomFilter0);
        assertTrue(boolean1 == boolean0);
    }
}
