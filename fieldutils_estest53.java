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

public class FieldUtils_ESTestTest53 extends FieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test52() throws Throwable {
        DateTimeZone dateTimeZone0 = DateTimeZone.UTC;
        IslamicChronology islamicChronology0 = IslamicChronology.getInstance(dateTimeZone0);
        ZonedChronology zonedChronology0 = ZonedChronology.getInstance(islamicChronology0, dateTimeZone0);
        DateTimeField dateTimeField0 = zonedChronology0.minuteOfHour();
        // Undeclared exception!
        try {
            FieldUtils.verifyValueBounds(dateTimeField0, 1, 1, (-1));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Value 1 for minuteOfHour must be in the range [1,-1]
            //
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }
}
