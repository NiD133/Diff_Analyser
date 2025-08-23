package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest1 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        UnparseableExtraFieldData unparseableExtraFieldData0 = new UnparseableExtraFieldData();
        ZipShort zipShort0 = unparseableExtraFieldData0.getHeaderId();
        ZipExtraField zipExtraField0 = ExtraFieldUtils.createExtraField(zipShort0);
        assertFalse(zipExtraField0.equals((Object) unparseableExtraFieldData0));
    }
}
