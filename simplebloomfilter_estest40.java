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

public class SimpleBloomFilter_ESTestTest40 extends SimpleBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test39() throws Throwable {
        Shape shape0 = Shape.fromKM(655, 1);
        EnhancedDoubleHasher enhancedDoubleHasher0 = new EnhancedDoubleHasher(655, 655);
        SimpleBloomFilter simpleBloomFilter0 = new SimpleBloomFilter(shape0);
        // Undeclared exception!
        try {
            simpleBloomFilter0.merge((Hasher) enhancedDoubleHasher0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // IndexExtractor should only send values in the range[0,1)
            //
            verifyException("org.apache.commons.collections4.bloomfilter.SimpleBloomFilter", e);
        }
    }
}
