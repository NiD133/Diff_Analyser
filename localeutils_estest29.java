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

public class LocaleUtils_ESTestTest29 extends LocaleUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        // Undeclared exception!
        try {
            LocaleUtils.toLocale("DX\"n{f!cA_1");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Invalid locale format: DX\"n{f!cA_1
            //
            verifyException("org.apache.commons.lang3.LocaleUtils", e);
        }
    }
}
