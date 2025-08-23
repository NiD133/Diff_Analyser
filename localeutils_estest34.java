package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LocaleUtils_ESTestTest34 extends LocaleUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        // Undeclared exception!
        try {
            LocaleUtils.toLocale("bi-K-D9");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Invalid locale format: bi-K-D9
            //
            verifyException("org.apache.commons.lang3.LocaleUtils", e);
        }
    }
}
