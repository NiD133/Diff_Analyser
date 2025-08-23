package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.TimeZone;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Weeks;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.chrono.LenientChronology;
import org.joda.time.chrono.ZonedChronology;
import org.junit.runner.RunWith;

public class PreciseDurationDateTimeField_ESTestTest23 extends PreciseDurationDateTimeField_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.halfdayOfDay();
        DurationFieldType durationFieldType0 = DurationFieldType.years();
        DurationField durationField0 = durationFieldType0.getField((Chronology) null);
        PreciseDateTimeField preciseDateTimeField0 = null;
        try {
            preciseDateTimeField0 = new PreciseDateTimeField(dateTimeFieldType0, durationField0, durationField0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Unit duration field must be precise
            //
            verifyException("org.joda.time.field.PreciseDurationDateTimeField", e);
        }
    }
}
