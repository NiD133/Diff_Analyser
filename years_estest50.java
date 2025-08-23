package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest50 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test49() throws Throwable {
        Years years0 = Years.ZERO;
        // Undeclared exception!
        try {
            years0.MIN_VALUE.negated();
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // Integer.MIN_VALUE cannot be negated
            //
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }
}
