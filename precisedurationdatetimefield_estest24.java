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

public class PreciseDurationDateTimeField_ESTestTest24 extends PreciseDurationDateTimeField_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.minuteOfDay();
        Weeks weeks0 = Weeks.ONE;
        DurationFieldType durationFieldType0 = weeks0.getFieldType();
        GJChronology gJChronology0 = GJChronology.getInstance();
        DurationField durationField0 = durationFieldType0.getField(gJChronology0);
        ScaledDurationField scaledDurationField0 = new ScaledDurationField(durationField0, durationFieldType0, 1058);
        PreciseDateTimeField preciseDateTimeField0 = new PreciseDateTimeField(dateTimeFieldType0, durationField0, scaledDurationField0);
        // Undeclared exception!
        try {
            preciseDateTimeField0.set((long) 1058, 1058);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Value 1058 for minuteOfDay must be in the range [0,1057]
            //
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }
}
