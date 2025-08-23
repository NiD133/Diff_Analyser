package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest8 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        ZipExtraField[] zipExtraFieldArray0 = new ZipExtraField[0];
        byte[] byteArray0 = ExtraFieldUtils.mergeLocalFileDataData(zipExtraFieldArray0);
        ExtraFieldUtils.UnparseableExtraField extraFieldUtils_UnparseableExtraField0 = ExtraFieldUtils.UnparseableExtraField.READ;
        ZipExtraField zipExtraField0 = extraFieldUtils_UnparseableExtraField0.onUnparseableExtraField(byteArray0, 0, 0, false, 0);
        assertNotNull(zipExtraField0);
        ExtraFieldUtils.fillExtraField(zipExtraField0, byteArray0, 0, 0, true);
        assertEquals(0, byteArray0.length);
        assertArrayEquals(new byte[] {}, byteArray0);
    }
}
