package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.TimeZone;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.tz.UTCProvider;
import org.junit.runner.RunWith;

public class IslamicChronology_ESTestTest13 extends IslamicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        TimeZone timeZone0 = TimeZone.getTimeZone("");
        DateTimeZone dateTimeZone0 = DateTimeZone.forTimeZone(timeZone0);
        IslamicChronology islamicChronology0 = IslamicChronology.getInstance(dateTimeZone0);
        IslamicChronology islamicChronology1 = IslamicChronology.getInstance(dateTimeZone0, islamicChronology0.LEAP_YEAR_HABASH_AL_HASIB);
        boolean boolean0 = islamicChronology1.isLeapYear(30);
        assertTrue(boolean0);
    }
}
