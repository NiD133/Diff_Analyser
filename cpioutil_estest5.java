package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CpioUtil_ESTestTest5 extends CpioUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        // Undeclared exception!
        try {
            CpioUtil.long2byteArray(0L, (-3182), true);
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.compress.archivers.cpio.CpioUtil", e);
        }
    }
}
