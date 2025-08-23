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

public class CachedDateTimeZone_ESTestTest27 extends CachedDateTimeZone_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        DateTimeZone dateTimeZone0 = DateTimeZone.forID("WET");
        CachedDateTimeZone cachedDateTimeZone0 = CachedDateTimeZone.forZone(dateTimeZone0);
        assertSame(dateTimeZone0, cachedDateTimeZone0);
    }
}
