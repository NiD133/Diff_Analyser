package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IEEE754rUtils_ESTestTest28 extends IEEE754rUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        // Undeclared exception!
        try {
            IEEE754rUtils.max((double[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // array
            //
            verifyException("java.util.Objects", e);
        }
    }
}
