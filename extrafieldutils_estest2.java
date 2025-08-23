package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest2 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        ExtraFieldUtils.UnparseableExtraField extraFieldUtils_UnparseableExtraField0 = ExtraFieldUtils.UnparseableExtraField.READ;
        byte[] byteArray0 = new byte[8];
        ZipExtraField zipExtraField0 = extraFieldUtils_UnparseableExtraField0.onUnparseableExtraField(byteArray0, (byte) 0, (byte) 7, false, (byte) 7);
        ZipExtraField[] zipExtraFieldArray0 = new ZipExtraField[2];
        zipExtraFieldArray0[1] = zipExtraField0;
        // Undeclared exception!
        try {
            ExtraFieldUtils.mergeLocalFileDataData(zipExtraFieldArray0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.compress.archivers.zip.ExtraFieldUtils", e);
        }
    }
}
