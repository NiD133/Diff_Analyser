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

public class IslamicChronology_ESTestTest4 extends IslamicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        IslamicChronology.LeapYearPatternType islamicChronology_LeapYearPatternType0 = IslamicChronology.LEAP_YEAR_15_BASED;
        IslamicChronology islamicChronology0 = new IslamicChronology((Chronology) null, (Object) null, islamicChronology_LeapYearPatternType0);
        long long0 = islamicChronology0.getYearMonthDayMillis(292271022, (-1), 273);
        assertEquals(8948501182656000000L, long0);
    }
}
