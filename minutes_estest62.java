package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest62 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test61() throws Throwable {
        Minutes minutes0 = Minutes.MAX_VALUE;
        // Undeclared exception!
        try {
            minutes0.multipliedBy((-4433));
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // Multiplication overflows an int: 2147483647 * -4433
            //
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }
}
