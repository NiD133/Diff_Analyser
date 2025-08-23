package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SegmentUtils_ESTestTest31 extends SegmentUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        // Undeclared exception!
        try {
            SegmentUtils.countInvokeInterfaceArgs("Aw2<'N6_{7~h_K?(gZ");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // No arguments
            //
            verifyException("org.apache.commons.compress.harmony.unpack200.SegmentUtils", e);
        }
    }
}
