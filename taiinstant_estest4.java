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

public class TaiInstant_ESTestTest4 extends TaiInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        Instant instant0 = MockInstant.now();
        TaiInstant taiInstant0 = TaiInstant.of(instant0);
        Duration duration0 = Duration.ofHours((-3507L));
        TaiInstant taiInstant1 = taiInstant0.minus(duration0);
        boolean boolean0 = taiInstant1.isBefore(taiInstant0);
        assertEquals(320000000, taiInstant1.getNano());
        assertEquals(1783725716L, taiInstant1.getTaiSeconds());
        assertFalse(boolean0);
        assertEquals(320000000, taiInstant0.getNano());
    }
}
