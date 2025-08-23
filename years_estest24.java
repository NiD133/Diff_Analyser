package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest24 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        Years years0 = Years.MAX_VALUE;
        // Undeclared exception!
        try {
            years0.plus(80);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // The calculation caused an overflow: 2147483647 + 80
            //
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }
}
