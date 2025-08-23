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

public class CachedDateTimeZone_ESTestTest30 extends CachedDateTimeZone_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        Instant instant0 = Instant.now();
        DateTimeZone dateTimeZone0 = instant0.getZone();
        CachedDateTimeZone cachedDateTimeZone0 = CachedDateTimeZone.forZone(dateTimeZone0);
        DateTimeZone dateTimeZone1 = cachedDateTimeZone0.getUncachedZone();
        assertSame(dateTimeZone1, dateTimeZone0);
    }
}
