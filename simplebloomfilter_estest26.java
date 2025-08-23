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

public class SimpleBloomFilter_ESTestTest26 extends SimpleBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        Shape shape0 = Shape.fromNM(49, 49);
        Shape shape1 = Shape.fromKM(49, 5906);
        SimpleBloomFilter simpleBloomFilter0 = new SimpleBloomFilter(shape1);
        SimpleBloomFilter simpleBloomFilter1 = new SimpleBloomFilter(shape0);
        // Undeclared exception!
        try {
            simpleBloomFilter1.merge((BloomFilter<?>) simpleBloomFilter0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // BitMapExtractor should send at most 1 maps
            //
            verifyException("org.apache.commons.collections4.bloomfilter.SimpleBloomFilter", e);
        }
    }
}
