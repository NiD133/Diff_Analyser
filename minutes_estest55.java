package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest55 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test54() throws Throwable {
        // Undeclared exception!
        try {
            Minutes.parseMinutes("UT");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Invalid format: \"UT\"
            //
            verifyException("org.joda.time.format.PeriodFormatter", e);
        }
    }
}
