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

public class TaiInstant_ESTestTest15 extends TaiInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        UtcInstant utcInstant0 = UtcInstant.ofModifiedJulianDay(774L, 1L);
        UtcInstant utcInstant1 = utcInstant0.withModifiedJulianDay((-2L));
        TaiInstant taiInstant0 = TaiInstant.of(utcInstant1);
        UtcInstant utcInstant2 = taiInstant0.toUtcInstant();
        assertEquals(1L, utcInstant2.getNanoOfDay());
        assertEquals(1, taiInstant0.getNano());
    }
}
