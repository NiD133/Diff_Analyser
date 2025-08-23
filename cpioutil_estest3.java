package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CpioUtil_ESTestTest3 extends CpioUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        byte[] byteArray0 = new byte[8];
        byteArray0[1] = (byte) (-51);
        long long0 = CpioUtil.byteArray2long(byteArray0, false);
        assertEquals((-3674937295934324736L), long0);
    }
}
