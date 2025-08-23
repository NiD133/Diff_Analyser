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

public class CachedDateTimeZone_ESTestTest12 extends CachedDateTimeZone_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        DateTimeZone dateTimeZone0 = DateTimeZone.UTC;
        CachedDateTimeZone cachedDateTimeZone0 = CachedDateTimeZone.forZone(dateTimeZone0);
        int int0 = cachedDateTimeZone0.getOffset((long) 0);
        assertEquals(0, int0);
    }
}