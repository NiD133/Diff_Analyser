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

public class FieldUtils_ESTestTest32 extends FieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        RoundingMode roundingMode0 = RoundingMode.HALF_EVEN;
        // Undeclared exception!
        try {
            FieldUtils.safeDivide(0L, 0L, roundingMode0);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
