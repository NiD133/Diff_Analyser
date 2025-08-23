package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SegmentUtils_ESTestTest1 extends SegmentUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        long[][] longArray0 = new long[4][7];
        long[] longArray1 = new long[0];
        longArray0[0] = longArray1;
        // Undeclared exception!
        try {
            SegmentUtils.countMatches(longArray0, (IMatcher) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentUtils", e);
        }
    }
}
