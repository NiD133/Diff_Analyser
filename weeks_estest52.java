package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest52 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test51() throws Throwable {
        // Undeclared exception!
        try {
            Weeks.parseWeeks(")%X[WS");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Invalid format: \")%X[WS\"
            //
            verifyException("org.joda.time.format.PeriodFormatter", e);
        }
    }
}
