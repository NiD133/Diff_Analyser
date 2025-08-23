package org.joda.time.base;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Date;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.util.MockGregorianCalendar;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MonthDay;
import org.joda.time.Partial;
import org.joda.time.ReadablePartial;
import org.joda.time.Weeks;
import org.joda.time.YearMonth;
import org.joda.time.Years;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimePrinter;
import org.junit.runner.RunWith;

public class AbstractPartial_ESTestTest17 extends AbstractPartial_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        GregorianChronology gregorianChronology0 = GregorianChronology.getInstance();
        YearMonth yearMonth0 = YearMonth.now();
        LocalDateTime localDateTime0 = new LocalDateTime((Chronology) gregorianChronology0);
        // Undeclared exception!
        try {
            localDateTime0.isAfter(yearMonth0);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // ReadablePartial objects must have matching field types
            //
            verifyException("org.joda.time.base.AbstractPartial", e);
        }
    }
}
