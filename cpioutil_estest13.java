package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CpioUtil_ESTestTest13 extends CpioUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        byte[] byteArray0 = CpioUtil.long2byteArray(1L, 5174, true);
        CpioUtil.byteArray2long(byteArray0, true);
        // Undeclared exception!
        CpioUtil.byteArray2long(byteArray0, true);
    }
}