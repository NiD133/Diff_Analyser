package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IndexExtractor_ESTestTest13 extends IndexExtractor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        long[] longArray0 = new long[18];
        longArray0[2] = (-1L);
        BitMapExtractor bitMapExtractor0 = BitMapExtractor.fromBitMapArray(longArray0);
        IndexExtractor indexExtractor0 = IndexExtractor.fromBitMapExtractor(bitMapExtractor0);
        Shape shape0 = Shape.fromNM(2147479552, 2147479552);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        boolean boolean0 = sparseBloomFilter0.contains(indexExtractor0);
        assertFalse(boolean0);
    }
}
