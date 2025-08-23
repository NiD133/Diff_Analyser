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

public class PreciseDurationDateTimeField_ESTestTest9 extends PreciseDurationDateTimeField_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.dayOfWeek();
        GregorianChronology gregorianChronology0 = GregorianChronology.getInstanceUTC();
        LenientChronology lenientChronology0 = LenientChronology.getInstance(gregorianChronology0);
        DateTimeZone dateTimeZone0 = DateTimeZone.forID((String) null);
        ZonedChronology zonedChronology0 = ZonedChronology.getInstance(lenientChronology0, dateTimeZone0);
        DurationField durationField0 = zonedChronology0.minutes();
        DurationField durationField1 = zonedChronology0.halfdays();
        PreciseDateTimeField preciseDateTimeField0 = new PreciseDateTimeField(dateTimeFieldType0, durationField0, durationField1);
        long long0 = preciseDateTimeField0.roundFloor(992280585600000L);
        assertEquals(992280585600000L, long0);
        assertFalse(preciseDateTimeField0.isLenient());
    }
}
