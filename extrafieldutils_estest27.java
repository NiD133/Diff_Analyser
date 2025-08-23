package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest27 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        Zip64ExtendedInformationExtraField zip64ExtendedInformationExtraField0 = new Zip64ExtendedInformationExtraField((ZipEightByteInteger) null, (ZipEightByteInteger) null);
        // Undeclared exception!
        try {
            ExtraFieldUtils.fillExtraField(zip64ExtendedInformationExtraField0, (byte[]) null, (-1811), (-1811), false);
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField", e);
        }
    }
}
