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

public class SimpleBloomFilter_ESTestTest16 extends SimpleBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        Shape shape0 = Shape.fromNM(38, 38);
        SimpleBloomFilter simpleBloomFilter0 = new SimpleBloomFilter(shape0);
        // Undeclared exception!
        try {
            simpleBloomFilter0.processIndices((IntPredicate) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // consumer
            //
            verifyException("java.util.Objects", e);
        }
    }
}
