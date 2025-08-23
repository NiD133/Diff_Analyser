package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest18 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        ZipExtraField[] zipExtraFieldArray0 = new ZipExtraField[1];
        Zip64ExtendedInformationExtraField zip64ExtendedInformationExtraField0 = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger zipEightByteInteger0 = ZipEightByteInteger.ZERO;
        zip64ExtendedInformationExtraField0.setSize(zipEightByteInteger0);
        zipExtraFieldArray0[0] = (ZipExtraField) zip64ExtendedInformationExtraField0;
        // Undeclared exception!
        try {
            ExtraFieldUtils.mergeLocalFileDataData(zipExtraFieldArray0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Zip64 extended information must contain both size values in the local file header.
            //
            verifyException("org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField", e);
        }
    }
}
