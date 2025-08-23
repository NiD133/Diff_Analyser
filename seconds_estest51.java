package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest51 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test50() throws Throwable {
        // Undeclared exception!
        try {
            Seconds.parseSeconds("");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Invalid format: \"\"
            //
            verifyException("org.joda.time.format.PeriodFormatter", e);
        }
    }
}
