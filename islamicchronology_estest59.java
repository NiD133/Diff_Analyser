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

public class IslamicChronology_ESTestTest59 extends IslamicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test58() throws Throwable {
        IslamicChronology islamicChronology0 = IslamicChronology.getInstanceUTC();
        IslamicChronology islamicChronology1 = (IslamicChronology) islamicChronology0.withZone((DateTimeZone) null);
        assertEquals(1, IslamicChronology.AH);
    }
}
