package org.joda.time.tz;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.GregorianChronology;
import org.junit.runner.RunWith;

public class CachedDateTimeZone_ESTestTest10 extends CachedDateTimeZone_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        DateTimeZone dateTimeZone0 = DateTimeZone.forID("WET");
        int int0 = dateTimeZone0.getStandardOffset(999999992896684031L);
        assertEquals(0, int0);
    }
}
