package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest33 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        ZipExtraField[] zipExtraFieldArray0 = new ZipExtraField[0];
        byte[] byteArray0 = ExtraFieldUtils.mergeCentralDirectoryData(zipExtraFieldArray0);
        assertEquals(0, byteArray0.length);
    }
}
