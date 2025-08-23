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

public class SimpleBloomFilter_ESTestTest46 extends SimpleBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test45() throws Throwable {
        Shape shape0 = Shape.fromNM(2147483605, 2147483605);
        SimpleBloomFilter simpleBloomFilter0 = new SimpleBloomFilter(shape0);
        long[] longArray0 = simpleBloomFilter0.asBitMapArray();
        assertEquals(33554432, longArray0.length);
    }
}
