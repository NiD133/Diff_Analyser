package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LZMAUtils_ESTestTest20 extends LZMAUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        boolean boolean0 = LZMAUtils.isCompressedFilename("Jz[y2){c^no");
        assertFalse(boolean0);
    }
}
