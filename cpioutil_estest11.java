package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CpioUtil_ESTestTest11 extends CpioUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        byte[] byteArray0 = new byte[4];
        long long0 = CpioUtil.byteArray2long(byteArray0, false);
        assertEquals(0L, long0);
    }
}
