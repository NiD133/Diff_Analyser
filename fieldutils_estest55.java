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

public class FieldUtils_ESTestTest55 extends FieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test54() throws Throwable {
        // Undeclared exception!
        try {
            FieldUtils.safeToInt(9223372036854775785L);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // Value cannot fit in an int: 9223372036854775785
            //
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }
}
