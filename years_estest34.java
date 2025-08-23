package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest34 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        Years years0 = Years.ONE;
        // Undeclared exception!
        try {
            years0.dividedBy(0);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // / by zero
            //
            verifyException("org.joda.time.Years", e);
        }
    }
}
