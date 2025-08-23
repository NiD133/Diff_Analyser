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

public class IslamicChronology_ESTestTest37 extends IslamicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test36() throws Throwable {
        IslamicChronology.LeapYearPatternType islamicChronology_LeapYearPatternType0 = IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB;
        IslamicChronology islamicChronology0 = IslamicChronology.getInstance();
        boolean boolean0 = islamicChronology0.LEAP_YEAR_INDIAN.equals(islamicChronology_LeapYearPatternType0);
        assertFalse(boolean0);
    }
}
