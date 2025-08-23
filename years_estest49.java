package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest49 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test48() throws Throwable {
        Years years0 = Years.MAX_VALUE;
        // Undeclared exception!
        try {
            years0.multipliedBy(3);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // Multiplication overflows an int: 2147483647 * 3
            //
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }
}
