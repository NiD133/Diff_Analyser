package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest34 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        byte[] byteArray0 = new byte[5];
        ZipExtraField[] zipExtraFieldArray0 = ExtraFieldUtils.parse(byteArray0, false);
        assertEquals(1, zipExtraFieldArray0.length);
    }
}
