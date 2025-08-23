package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IndexExtractor_ESTestTest11 extends IndexExtractor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        int[] intArray0 = new int[1];
        IndexExtractor indexExtractor0 = IndexExtractor.fromIndexArray(intArray0);
        Shape shape0 = Shape.fromNM(1624, 1624);
        ArrayCountingBloomFilter arrayCountingBloomFilter0 = new ArrayCountingBloomFilter(shape0);
        boolean boolean0 = arrayCountingBloomFilter0.contains(indexExtractor0);
        assertFalse(boolean0);
    }
}
