package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LZMAUtils_ESTestTest5 extends LZMAUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        String string0 = LZMAUtils.getUncompressedFilename("CACHED_UNAVAILABLE");
        assertEquals("CACHED_UNAVAILABLE", string0);
    }
}
