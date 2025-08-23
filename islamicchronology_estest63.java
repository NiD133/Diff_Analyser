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

public class IslamicChronology_ESTestTest63 extends IslamicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test62() throws Throwable {
        IslamicChronology islamicChronology0 = new IslamicChronology((Chronology) null, (Object) null, (IslamicChronology.LeapYearPatternType) null);
        // Undeclared exception!
        try {
            islamicChronology0.getDaysInYear(1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
