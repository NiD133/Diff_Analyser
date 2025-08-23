package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest21 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        ResourceAlignmentExtraField resourceAlignmentExtraField0 = new ResourceAlignmentExtraField();
        byte[] byteArray0 = new byte[0];
        try {
            ExtraFieldUtils.fillExtraField(resourceAlignmentExtraField0, byteArray0, 2, 2, false);
            fail("Expecting exception: ZipException");
        } catch (ZipException e) {
            //
            // Failed to parse corrupt ZIP extra field of type a11e
            //
            verifyException("org.apache.commons.compress.archivers.zip.ZipUtil", e);
        }
    }
}
