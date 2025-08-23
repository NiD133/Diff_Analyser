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

public class FieldUtils_ESTestTest80 extends FieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test79() throws Throwable {
        // Undeclared exception!
        try {
            FieldUtils.safeSubtract((-9223372036854775808L), 11L);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // The calculation caused an overflow: -9223372036854775808 - 11
            //
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }
}
