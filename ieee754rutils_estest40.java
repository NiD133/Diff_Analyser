package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IEEE754rUtils_ESTestTest40 extends IEEE754rUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test39() throws Throwable {
        double[] doubleArray0 = new double[0];
        // Undeclared exception!
        try {
            IEEE754rUtils.max(doubleArray0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Array cannot be empty.
            //
            verifyException("org.apache.commons.lang3.Validate", e);
        }
    }
}
