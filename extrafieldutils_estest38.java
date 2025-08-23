package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest38 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test37() throws Throwable {
        AsiExtraField asiExtraField0 = new AsiExtraField();
        byte[] byteArray0 = asiExtraField0.getCentralDirectoryData();
        ExtraFieldUtils.UnparseableExtraField extraFieldUtils_UnparseableExtraField0 = ExtraFieldUtils.UnparseableExtraField.READ;
        ZipExtraField[] zipExtraFieldArray0 = ExtraFieldUtils.parse(byteArray0, false, extraFieldUtils_UnparseableExtraField0);
        byte[] byteArray1 = ExtraFieldUtils.mergeCentralDirectoryData(zipExtraFieldArray0);
        assertEquals(14, byteArray1.length);
    }
}
