package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest51 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test50() throws Throwable {
        // Undeclared exception!
        try {
            Years.yearsBetween((ReadableInstant) null, (ReadableInstant) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // ReadableInstant objects must not be null
            //
            verifyException("org.joda.time.base.BaseSingleFieldPeriod", e);
        }
    }
}
