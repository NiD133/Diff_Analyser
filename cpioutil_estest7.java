package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CpioUtil_ESTestTest7 extends CpioUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        byte[] byteArray0 = new byte[0];
        // Undeclared exception!
        try {
            CpioUtil.byteArray2long(byteArray0, false);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 0
            //
            verifyException("org.apache.commons.compress.archivers.cpio.CpioUtil", e);
        }
    }
}
