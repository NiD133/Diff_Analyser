package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.System;
import org.evosuite.runtime.mock.java.time.MockZonedDateTime;
import org.evosuite.runtime.mock.java.util.MockDate;
import org.evosuite.runtime.mock.java.util.MockGregorianCalendar;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.runner.RunWith;

public class CalendarConverter_ESTestTest10 extends CalendarConverter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        CalendarConverter calendarConverter0 = new CalendarConverter();
        DateTimeZone dateTimeZone0 = DateTimeZone.forOffsetMillis(1963);
        MockGregorianCalendar mockGregorianCalendar0 = new MockGregorianCalendar(0, 1963, 0, (-247581896), (-21), 3826);
        ZonedDateTime zonedDateTime0 = mockGregorianCalendar0.toZonedDateTime();
        GregorianCalendar gregorianCalendar0 = MockGregorianCalendar.from(zonedDateTime0);
        Chronology chronology0 = calendarConverter0.getChronology((Object) gregorianCalendar0, dateTimeZone0);
        assertNotNull(chronology0);
    }
}
