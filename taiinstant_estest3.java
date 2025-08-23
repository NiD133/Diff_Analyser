package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockInstant;
import org.junit.runner.RunWith;

public class TaiInstant_ESTestTest3 extends TaiInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Instant instant0 = MockInstant.now();
        TaiInstant taiInstant0 = TaiInstant.of(instant0);
        UtcInstant utcInstant0 = UtcInstant.of(taiInstant0);
        UtcInstant utcInstant1 = utcInstant0.withModifiedJulianDay(0L);
        TaiInstant taiInstant1 = TaiInstant.of(utcInstant1);
        boolean boolean0 = taiInstant1.equals(taiInstant0);
        assertFalse(taiInstant0.equals((Object) taiInstant1));
        assertFalse(boolean0);
        assertEquals(73281320000000L, utcInstant1.getNanoOfDay());
    }
}