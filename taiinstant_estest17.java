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

public class TaiInstant_ESTestTest17 extends TaiInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        TaiInstant taiInstant0 = TaiInstant.ofTaiSeconds(41316L, (-1L));
        Duration duration0 = Duration.ofDays(41317L);
        Duration duration1 = duration0.multipliedBy((-187L));
        TaiInstant taiInstant1 = taiInstant0.plus(duration1);
        assertEquals((-667550464285L), taiInstant1.getTaiSeconds());
        assertEquals(999999999, taiInstant0.getNano());
        assertEquals(41315L, taiInstant0.getTaiSeconds());
        assertEquals(999999999, taiInstant1.getNano());
    }
}
