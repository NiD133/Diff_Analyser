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

public class IslamicChronology_ESTestTest27 extends IslamicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        CopticChronology copticChronology0 = CopticChronology.getInstanceUTC();
        IslamicChronology islamicChronology0 = new IslamicChronology(copticChronology0, copticChronology0, (IslamicChronology.LeapYearPatternType) null);
        // Undeclared exception!
        try {
            islamicChronology0.setYear(380L, 1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
