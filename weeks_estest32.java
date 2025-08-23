package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest32 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        Weeks weeks0 = Weeks.MAX_VALUE;
        // Undeclared exception!
        try {
            weeks0.plus(weeks0);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // The calculation caused an overflow: 2147483647 + 2147483647
            //
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }
}
