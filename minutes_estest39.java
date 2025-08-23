package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest39 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        Minutes minutes0 = Minutes.minutesIn((ReadableInterval) null);
        // Undeclared exception!
        try {
            minutes0.dividedBy(0);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // / by zero
            //
            verifyException("org.joda.time.Minutes", e);
        }
    }
}
