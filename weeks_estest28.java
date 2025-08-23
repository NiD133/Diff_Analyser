package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest28 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        // Undeclared exception!
        try {
            Weeks.weeksBetween((ReadablePartial) null, (ReadablePartial) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // ReadablePartial objects must not be null
            //
            verifyException("org.joda.time.base.BaseSingleFieldPeriod", e);
        }
    }
}
