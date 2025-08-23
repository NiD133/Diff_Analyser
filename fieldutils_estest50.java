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

public class FieldUtils_ESTestTest50 extends FieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test49() throws Throwable {
        DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.minuteOfHour();
        // Undeclared exception!
        try {
            FieldUtils.verifyValueBounds(dateTimeFieldType0, 1428, (-1584), (-473));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Value 1428 for minuteOfHour must be in the range [-1584,-473]
            //
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }
}
