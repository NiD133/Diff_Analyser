package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest39 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        // Undeclared exception!
        try {
            Years.parseYears("");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Invalid format: \"\"
            //
            verifyException("org.joda.time.format.PeriodFormatter", e);
        }
    }
}
