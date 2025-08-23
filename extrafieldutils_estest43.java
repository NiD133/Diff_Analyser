package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest43 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test42() throws Throwable {
        X000A_NTFS x000A_NTFS0 = new X000A_NTFS();
        ZipExtraField[] zipExtraFieldArray0 = new ZipExtraField[6];
        zipExtraFieldArray0[0] = (ZipExtraField) x000A_NTFS0;
        zipExtraFieldArray0[1] = (ZipExtraField) x000A_NTFS0;
        zipExtraFieldArray0[2] = (ZipExtraField) x000A_NTFS0;
        zipExtraFieldArray0[3] = (ZipExtraField) x000A_NTFS0;
        zipExtraFieldArray0[4] = (ZipExtraField) x000A_NTFS0;
        zipExtraFieldArray0[5] = (ZipExtraField) x000A_NTFS0;
        byte[] byteArray0 = ExtraFieldUtils.mergeCentralDirectoryData(zipExtraFieldArray0);
        ZipExtraField[] zipExtraFieldArray1 = ExtraFieldUtils.parse(byteArray0);
        assertEquals(6, zipExtraFieldArray1.length);
    }
}