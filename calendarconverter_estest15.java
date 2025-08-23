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

public class CalendarConverter_ESTestTest15 extends CalendarConverter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        CalendarConverter calendarConverter0 = new CalendarConverter();
        Object object0 = new Object();
        // Undeclared exception!
        try {
            calendarConverter0.getInstantMillis(object0, (Chronology) null);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // java.lang.Object cannot be cast to java.util.Calendar
            //
            verifyException("org.joda.time.convert.CalendarConverter", e);
        }
    }
}
