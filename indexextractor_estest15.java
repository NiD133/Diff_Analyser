package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IndexExtractor_ESTestTest15 extends IndexExtractor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        int[] intArray0 = new int[4];
        IndexExtractor indexExtractor0 = IndexExtractor.fromIndexArray(intArray0);
        IndexExtractor indexExtractor1 = indexExtractor0.uniqueIndices();
        IndexExtractor indexExtractor2 = indexExtractor1.uniqueIndices();
        assertSame(indexExtractor1, indexExtractor2);
    }
}