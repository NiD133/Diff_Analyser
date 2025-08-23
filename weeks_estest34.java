package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest34 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        Weeks weeks0 = Weeks.TWO;
        // Undeclared exception!
        try {
            weeks0.dividedBy(0);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // / by zero
            //
            verifyException("org.joda.time.Weeks", e);
        }
    }
}
