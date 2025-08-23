package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest46 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test45() throws Throwable {
        Seconds seconds0 = Seconds.MAX_VALUE;
        // Undeclared exception!
        try {
            seconds0.dividedBy(0);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // / by zero
            //
            verifyException("org.joda.time.Seconds", e);
        }
    }
}
