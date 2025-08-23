package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CpioUtil_ESTestTest14 extends CpioUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        long long0 = CpioUtil.fileType(61440L);
        assertEquals(61440L, long0);
    }
}
