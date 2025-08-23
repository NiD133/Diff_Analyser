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

public class PreciseDurationDateTimeField_ESTestTest11 extends PreciseDurationDateTimeField_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.yearOfCentury();
        JulianChronology julianChronology0 = JulianChronology.getInstance();
        DateTimeZone dateTimeZone0 = DateTimeZone.UTC;
        ZonedChronology zonedChronology0 = ZonedChronology.getInstance(julianChronology0, dateTimeZone0);
        DurationField durationField0 = zonedChronology0.days();
        Days days0 = Days.MIN_VALUE;
        DurationFieldType durationFieldType0 = days0.getFieldType();
        PreciseDurationField preciseDurationField0 = new PreciseDurationField(durationFieldType0, 21859200000L);
        PreciseDateTimeField preciseDateTimeField0 = new PreciseDateTimeField(dateTimeFieldType0, durationField0, preciseDurationField0);
        long long0 = preciseDateTimeField0.roundCeiling((-1481L));
        assertEquals(0L, long0);
    }
}
