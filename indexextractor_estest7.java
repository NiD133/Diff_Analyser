package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IndexExtractor_ESTestTest7 extends IndexExtractor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        IndexExtractor indexExtractor0 = IndexExtractor.fromIndexArray((int[]) null);
        // Undeclared exception!
        try {
            indexExtractor0.asIndexArray();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.collections4.bloomfilter.IndexExtractor$2", e);
        }
    }
}
