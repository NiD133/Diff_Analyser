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

public class TaiInstant_ESTestTest46 extends TaiInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test45() throws Throwable {
        TaiInstant taiInstant0 = TaiInstant.ofTaiSeconds(37L, 37L);
        TaiInstant taiInstant1 = TaiInstant.ofTaiSeconds(37L, 3503L);
        boolean boolean0 = taiInstant0.isBefore(taiInstant1);
        assertTrue(boolean0);
        assertEquals(37L, taiInstant1.getTaiSeconds());
        assertEquals(3503, taiInstant1.getNano());
    }
}
