package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IndexExtractor_ESTestTest1 extends IndexExtractor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Shape shape0 = Shape.fromKM(1856, 1856);
        SimpleBloomFilter simpleBloomFilter0 = new SimpleBloomFilter(shape0);
        int[] intArray0 = simpleBloomFilter0.asIndexArray();
        assertEquals(0, intArray0.length);
    }
}
