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

public class IslamicChronology_ESTestTest22 extends IslamicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        UTCProvider uTCProvider0 = new UTCProvider();
        GJChronology gJChronology0 = GJChronology.getInstance((DateTimeZone) null);
        IslamicChronology islamicChronology0 = new IslamicChronology(gJChronology0, uTCProvider0, (IslamicChronology.LeapYearPatternType) null);
        IslamicChronology.LeapYearPatternType islamicChronology_LeapYearPatternType0 = islamicChronology0.getLeapYearPatternType();
        assertNull(islamicChronology_LeapYearPatternType0);
    }
}
