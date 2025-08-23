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

public class IslamicChronology_ESTestTest11 extends IslamicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        DateTimeZone dateTimeZone0 = DateTimeZone.getDefault();
        IslamicChronology islamicChronology0 = IslamicChronology.getInstance(dateTimeZone0);
        Object object0 = new Object();
        IslamicChronology islamicChronology1 = new IslamicChronology(islamicChronology0, object0, islamicChronology0.LEAP_YEAR_15_BASED);
        boolean boolean0 = islamicChronology0.equals(islamicChronology1);
        assertFalse(islamicChronology1.equals((Object) islamicChronology0));
        assertFalse(boolean0);
    }
}
