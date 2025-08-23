package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LZMAUtils_ESTestTest16 extends LZMAUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        byte[] byteArray0 = new byte[16];
        byteArray0[0] = (byte) 93;
        boolean boolean0 = LZMAUtils.matches(byteArray0, (byte) 93);
        assertTrue(boolean0);
    }
}
