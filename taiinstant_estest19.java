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

public class TaiInstant_ESTestTest19 extends TaiInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        Duration duration0 = Duration.ofDays(41317L);
        Clock clock0 = MockClock.systemUTC();
        Instant instant0 = MockInstant.now(clock0);
        Instant instant1 = MockInstant.minus(instant0, (TemporalAmount) duration0);
        TaiInstant taiInstant0 = TaiInstant.of(instant1);
        assertEquals((-1798688309L), taiInstant0.getTaiSeconds());
        assertEquals(320000000, taiInstant0.getNano());
    }
}
