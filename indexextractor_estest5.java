package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IndexExtractor_ESTestTest5 extends IndexExtractor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        // Undeclared exception!
        try {
            IndexExtractor.fromBitMapExtractor((BitMapExtractor) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // bitMapExtractor
            //
            verifyException("java.util.Objects", e);
        }
    }
}
