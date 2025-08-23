package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IndexExtractor_ESTestTest9 extends IndexExtractor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        int[] intArray0 = new int[4];
        IndexExtractor indexExtractor0 = IndexExtractor.fromIndexArray(intArray0);
        IndexExtractor indexExtractor1 = indexExtractor0.uniqueIndices();
        Shape shape0 = Shape.fromKM(1867, 31);
        SimpleBloomFilter simpleBloomFilter0 = new SimpleBloomFilter(shape0);
        boolean boolean0 = simpleBloomFilter0.contains(indexExtractor1);
        assertFalse(boolean0);
    }
}
