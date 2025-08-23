package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest13 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        AsiExtraField asiExtraField0 = new AsiExtraField();
        byte[] byteArray0 = asiExtraField0.getLocalFileDataData();
        ZipArchiveEntry.ExtraFieldParsingMode zipArchiveEntry_ExtraFieldParsingMode0 = ZipArchiveEntry.ExtraFieldParsingMode.DRACONIC;
        try {
            ExtraFieldUtils.parse(byteArray0, true, (ExtraFieldParsingBehavior) zipArchiveEntry_ExtraFieldParsingMode0);
            fail("Expecting exception: ZipException");
        } catch (ZipException e) {
            //
            // Bad extra field starting at 0.  Block length of 58250 bytes exceeds remaining data of 10 bytes.
            //
            verifyException("org.apache.commons.compress.archivers.zip.ExtraFieldUtils$UnparseableExtraField", e);
        }
    }
}