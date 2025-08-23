package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IndexExtractor_ESTestTest4 extends IndexExtractor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        int[] intArray0 = new int[1];
        intArray0[0] = (-1);
        IndexExtractor indexExtractor0 = IndexExtractor.fromIndexArray(intArray0);
        // Undeclared exception!
        try {
            indexExtractor0.uniqueIndices();
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // bitIndex < 0: -1
            //
            verifyException("java.util.BitSet", e);
        }
    }
}
