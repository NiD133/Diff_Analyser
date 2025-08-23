package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest64 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test63() throws Throwable {
        Minutes minutes0 = Minutes.MAX_VALUE;
        // Undeclared exception!
        try {
            minutes0.minus((-2550));
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // The calculation caused an overflow: 2147483647 + 2550
            //
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }
}
