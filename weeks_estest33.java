package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest33 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        Weeks weeks0 = Weeks.MIN_VALUE;
        // Undeclared exception!
        try {
            weeks0.minus(weeks0);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // Integer.MIN_VALUE cannot be negated
            //
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }
}
