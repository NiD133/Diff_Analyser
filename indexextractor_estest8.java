package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IndexExtractor_ESTestTest8 extends IndexExtractor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        int[] intArray0 = new int[3];
        intArray0[0] = Integer.MAX_VALUE;
        IndexExtractor indexExtractor0 = IndexExtractor.fromIndexArray(intArray0);
        IndexExtractor indexExtractor1 = indexExtractor0.uniqueIndices();
        // Undeclared exception!
        try {
            indexExtractor1.asIndexArray();
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // fromIndex < 0: -2147483648
            //
            verifyException("java.util.BitSet", e);
        }
    }
}
