package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest40 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test39() throws Throwable {
        Minutes minutes0 = Minutes.TWO;
        // Undeclared exception!
        try {
            minutes0.plus(Integer.MAX_VALUE);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // The calculation caused an overflow: 2 + 2147483647
            //
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }
}