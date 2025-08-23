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

public class CachedDateTimeZone_ESTestTest1 extends CachedDateTimeZone_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        DateTimeZone dateTimeZone0 = DateTimeZone.getDefault();
        CachedDateTimeZone cachedDateTimeZone0 = CachedDateTimeZone.forZone(dateTimeZone0);
        String string0 = cachedDateTimeZone0.getName(212544000000L);
        assertEquals("Coordinated Universal Time", string0);
    }
}
