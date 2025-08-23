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

public class IslamicChronology_ESTestTest30 extends IslamicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        ISOChronology iSOChronology0 = ISOChronology.getInstanceUTC();
        IslamicChronology islamicChronology0 = new IslamicChronology(iSOChronology0, iSOChronology0, (IslamicChronology.LeapYearPatternType) null);
        // Undeclared exception!
        try {
            islamicChronology0.getMonthOfYear(918518400000L, 1138);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
