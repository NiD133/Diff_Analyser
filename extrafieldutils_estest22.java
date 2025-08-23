package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest22 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        X0019_EncryptionRecipientCertificateList x0019_EncryptionRecipientCertificateList0 = new X0019_EncryptionRecipientCertificateList();
        byte[] byteArray0 = new byte[0];
        // Undeclared exception!
        try {
            ExtraFieldUtils.fillExtraField(x0019_EncryptionRecipientCertificateList0, byteArray0, (-1100), (-1100), false);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // -1100 > -2200
            //
            verifyException("java.util.Arrays", e);
        }
    }
}
