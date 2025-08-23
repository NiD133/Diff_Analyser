package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest25 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        byte[] byteArray0 = new byte[9];
        byteArray0[3] = (byte) (-47);
        ZipArchiveEntry.ExtraFieldParsingMode zipArchiveEntry_ExtraFieldParsingMode0 = ZipArchiveEntry.ExtraFieldParsingMode.ONLY_PARSEABLE_LENIENT;
        ZipExtraField[] zipExtraFieldArray0 = ExtraFieldUtils.parse(byteArray0, true, (ExtraFieldParsingBehavior) zipArchiveEntry_ExtraFieldParsingMode0);
        assertEquals(0, zipExtraFieldArray0.length);
    }
}
