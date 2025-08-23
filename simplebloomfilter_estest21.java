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

public class SimpleBloomFilter_ESTestTest21 extends SimpleBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        Shape shape0 = Shape.fromKM(668, 668);
        SimpleBloomFilter simpleBloomFilter0 = new SimpleBloomFilter(shape0);
        // Undeclared exception!
        try {
            simpleBloomFilter0.merge((IndexExtractor) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // indexExtractor
            //
            verifyException("java.util.Objects", e);
        }
    }
}
