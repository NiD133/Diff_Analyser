package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest35 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test34() throws Throwable {
        Seconds seconds0 = Seconds.THREE;
        // Undeclared exception!
        try {
            seconds0.minus((-2147483646));
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // The calculation caused an overflow: 3 + 2147483646
            //
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }
}
