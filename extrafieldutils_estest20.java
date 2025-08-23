package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest20 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        byte[] byteArray0 = new byte[12];
        ZipExtraField[] zipExtraFieldArray0 = ExtraFieldUtils.parse(byteArray0);
        // Undeclared exception!
        try {
            ExtraFieldUtils.mergeCentralDirectoryData(zipExtraFieldArray0);
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            //
            // org/apache/commons/lang3/ArrayUtils
            //
            verifyException("org.apache.commons.compress.archivers.zip.ZipShort", e);
        }
    }
}
