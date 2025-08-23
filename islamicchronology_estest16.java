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

public class IslamicChronology_ESTestTest16 extends IslamicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        IslamicChronology islamicChronology0 = IslamicChronology.getInstance();
        int int0 = islamicChronology0.getYear((-69139612803234L));
        assertEquals((-839), int0);
    }
}
