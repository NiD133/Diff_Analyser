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

public class SimpleBloomFilter_ESTestTest29 extends SimpleBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        Shape shape0 = Shape.fromKM(20, 20);
        SimpleBloomFilter simpleBloomFilter0 = new SimpleBloomFilter(shape0);
        BitMapExtractor bitMapExtractor0 = BitMapExtractor.fromIndexExtractor(simpleBloomFilter0, 283);
        // Undeclared exception!
        try {
            simpleBloomFilter0.merge(bitMapExtractor0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // BitMapExtractor should send at most 1 maps
            //
            verifyException("org.apache.commons.collections4.bloomfilter.SimpleBloomFilter", e);
        }
    }
}
