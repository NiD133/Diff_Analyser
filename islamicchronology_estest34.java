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

public class IslamicChronology_ESTestTest34 extends IslamicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        DateTimeZone dateTimeZone0 = DateTimeZone.forOffsetHours(12);
        // Undeclared exception!
        try {
            IslamicChronology.getInstance(dateTimeZone0, (IslamicChronology.LeapYearPatternType) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.joda.time.chrono.IslamicChronology", e);
        }
    }
}
