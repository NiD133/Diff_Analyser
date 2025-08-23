package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest28 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        ResourceAlignmentExtraField resourceAlignmentExtraField0 = new ResourceAlignmentExtraField();
        // Undeclared exception!
        try {
            ExtraFieldUtils.fillExtraField(resourceAlignmentExtraField0, (byte[]) null, 2, 2, true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
