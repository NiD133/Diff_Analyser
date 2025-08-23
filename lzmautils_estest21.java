package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LZMAUtils_ESTestTest21 extends LZMAUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        LZMAUtils.setCacheLZMAAvailablity(false);
        boolean boolean0 = LZMAUtils.isLZMACompressionAvailable();
        assertFalse(boolean0);
    }
}
