package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest31 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        byte[] byteArray0 = new byte[8];
        ZipShort zipShort0 = new ZipShort(byteArray0);
        ZipExtraField zipExtraField0 = ExtraFieldUtils.createExtraField(zipShort0);
        assertNotNull(zipExtraField0);
    }
}
