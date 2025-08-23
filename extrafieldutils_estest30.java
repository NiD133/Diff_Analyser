package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest30 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        X000A_NTFS x000A_NTFS0 = new X000A_NTFS();
        ZipShort zipShort0 = x000A_NTFS0.getCentralDirectoryLength();
        ZipExtraField zipExtraField0 = ExtraFieldUtils.createExtraFieldNoDefault(zipShort0);
        assertNull(zipExtraField0);
    }
}