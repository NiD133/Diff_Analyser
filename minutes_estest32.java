package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest32 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        Minutes minutes0 = Minutes.MIN_VALUE;
        // Undeclared exception!
        try {
            minutes0.toStandardSeconds();
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // Multiplication overflows an int: -2147483648 * 60
            //
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }
}
