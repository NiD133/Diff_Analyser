package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.MonthDay;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableInterval;
import org.joda.time.MutablePeriod;
import org.joda.time.Partial;
import org.joda.time.PeriodType;
import org.joda.time.ReadWritableInterval;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.chrono.ZonedChronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimePrinter;
import org.junit.runner.RunWith;

public class StringConverter_ESTestTest38 extends StringConverter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test37() throws Throwable {
        StringConverter stringConverter0 = StringConverter.INSTANCE;
        EthiopicChronology ethiopicChronology0 = EthiopicChronology.getInstanceUTC();
        MonthDay monthDay0 = new MonthDay(2454L, (Chronology) ethiopicChronology0);
        DateTimePrinter dateTimePrinter0 = mock(DateTimePrinter.class, new ViolatedAssumptionAnswer());
        DateTimeParser dateTimeParser0 = mock(DateTimeParser.class, new ViolatedAssumptionAnswer());
        doReturn(1).when(dateTimeParser0).parseInto(any(org.joda.time.format.DateTimeParserBucket.class), anyString(), anyInt());
        DateTimeFormatter dateTimeFormatter0 = new DateTimeFormatter(dateTimePrinter0, dateTimeParser0);
        DateTimeFormatter dateTimeFormatter1 = dateTimeFormatter0.withZoneUTC();
        int[] intArray0 = stringConverter0.getPartialValues((ReadablePartial) monthDay0, (Object) "0", (Chronology) ethiopicChronology0, dateTimeFormatter1);
        assertArrayEquals(new int[] { 4, 23 }, intArray0);
    }
}
