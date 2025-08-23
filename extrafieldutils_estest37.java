package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest37 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test36() throws Throwable {
        byte[] byteArray0 = new byte[9];
        byteArray0[7] = (byte) 4;
        try {
            ExtraFieldUtils.parse(byteArray0, true);
            fail("Expecting exception: ZipException");
        } catch (ZipException e) {
            //
            // Bad extra field starting at 4.  Block length of 1024 bytes exceeds remaining data of 1 bytes.
            //
            verifyException("org.apache.commons.compress.archivers.zip.ExtraFieldUtils$UnparseableExtraField", e);
        }
    }
}
