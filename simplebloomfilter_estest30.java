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

public class SimpleBloomFilter_ESTestTest30 extends SimpleBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        int int0 = Integer.MAX_VALUE;
        Shape shape0 = Shape.fromNM(Integer.MAX_VALUE, Integer.MAX_VALUE);
        SimpleBloomFilter simpleBloomFilter0 = new SimpleBloomFilter(shape0);
        EnhancedDoubleHasher enhancedDoubleHasher0 = new EnhancedDoubleHasher((-154L), 2147483647L);
        simpleBloomFilter0.merge((Hasher) enhancedDoubleHasher0);
        // Undeclared exception!
        simpleBloomFilter0.isEmpty();
    }
}
