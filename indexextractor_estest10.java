package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IndexExtractor_ESTestTest10 extends IndexExtractor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        long[] longArray0 = new long[3];
        longArray0[2] = (-1L);
        BitMapExtractor bitMapExtractor0 = BitMapExtractor.fromBitMapArray(longArray0);
        IndexExtractor indexExtractor0 = IndexExtractor.fromBitMapExtractor(bitMapExtractor0);
        int[] intArray0 = indexExtractor0.asIndexArray();
        assertEquals(64, intArray0.length);
    }
}
