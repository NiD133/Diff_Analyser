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

public class IslamicChronology_ESTestTest52 extends IslamicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test51() throws Throwable {
        IslamicChronology.LeapYearPatternType islamicChronology_LeapYearPatternType0 = new IslamicChronology.LeapYearPatternType(8171, 8171);
        IslamicChronology islamicChronology0 = new IslamicChronology((Chronology) null, (Object) null, islamicChronology_LeapYearPatternType0);
        int int0 = islamicChronology0.getDayOfMonth(8171);
        assertEquals(19, int0);
    }
}
