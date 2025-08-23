package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.math.RoundingMode;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.chrono.ZonedChronology;
import org.junit.runner.RunWith;

public class FieldUtils_ESTestTest58 extends FieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test57() throws Throwable {
        RoundingMode roundingMode0 = RoundingMode.DOWN;
        // Undeclared exception!
        try {
            FieldUtils.safeDivide((-9223372036854775804L), (long) (-1), roundingMode0);
            //  fail("Expecting exception: ArithmeticException");
            // Unstable assertion
        } catch (ArithmeticException e) {
            //
            // Multiplication overflows a long: -9223372036854775804 / -1
            //
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }
}
