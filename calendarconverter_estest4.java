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

public class CalendarConverter_ESTestTest4 extends CalendarConverter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        System.setCurrentTimeMillis((-1870L));
        CalendarConverter calendarConverter0 = CalendarConverter.INSTANCE;
        TimeZone timeZone0 = TimeZone.getDefault();
        MockGregorianCalendar mockGregorianCalendar0 = new MockGregorianCalendar(timeZone0);
        long long0 = calendarConverter0.getInstantMillis(mockGregorianCalendar0, (Chronology) null);
        assertEquals((-1870L), long0);
    }
}
