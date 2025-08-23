package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest34 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        Seconds seconds0 = Seconds.MAX_VALUE;
        Seconds seconds1 = Seconds.MIN_VALUE;
        // Undeclared exception!
        try {
            seconds0.minus(seconds1);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // Integer.MIN_VALUE cannot be negated
            //
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }
}
