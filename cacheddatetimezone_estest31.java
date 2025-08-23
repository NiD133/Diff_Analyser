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

public class CachedDateTimeZone_ESTestTest31 extends CachedDateTimeZone_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        DateTimeZone dateTimeZone0 = DateTimeZone.forID("WET");
        GregorianChronology gregorianChronology0 = GregorianChronology.getInstance();
        LocalDateTime localDateTime0 = new LocalDateTime(9999998125080576L, (Chronology) gregorianChronology0);
        boolean boolean0 = dateTimeZone0.isLocalDateTimeGap(localDateTime0);
        assertFalse(boolean0);
    }
}
