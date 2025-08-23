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

public class SimpleBloomFilter_ESTestTest34 extends SimpleBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        Shape shape0 = Shape.fromNM(10, 10);
        SimpleBloomFilter simpleBloomFilter0 = new SimpleBloomFilter(shape0);
        int[] intArray0 = new int[8];
        intArray0[0] = 977;
        IndexExtractor indexExtractor0 = IndexExtractor.fromIndexArray(intArray0);
        // Undeclared exception!
        try {
            simpleBloomFilter0.contains(indexExtractor0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 15
            //
            verifyException("org.apache.commons.collections4.bloomfilter.BitMaps", e);
        }
    }
}
