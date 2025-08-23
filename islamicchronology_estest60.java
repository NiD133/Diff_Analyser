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

public class IslamicChronology_ESTestTest60 extends IslamicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test59() throws Throwable {
        IslamicChronology.LeapYearPatternType islamicChronology_LeapYearPatternType0 = new IslamicChronology.LeapYearPatternType(4497, 4497);
        // Undeclared exception!
        try {
            IslamicChronology.getInstance((DateTimeZone) null, islamicChronology_LeapYearPatternType0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // -111
            //
            verifyException("org.joda.time.chrono.IslamicChronology", e);
        }
    }
}
