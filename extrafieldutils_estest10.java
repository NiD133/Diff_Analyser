package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest10 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        Class<Object> class0 = Object.class;
        // Undeclared exception!
        try {
            ExtraFieldUtils.register(class0);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
        }
    }
}
