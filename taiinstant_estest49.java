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

public class TaiInstant_ESTestTest49 extends TaiInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test48() throws Throwable {
        TaiInstant taiInstant0 = TaiInstant.ofTaiSeconds(50L, 50L);
        TaiInstant taiInstant1 = TaiInstant.ofTaiSeconds((-1L), 1000000000L);
        boolean boolean0 = taiInstant1.isAfter(taiInstant0);
        assertEquals(50, taiInstant0.getNano());
        assertEquals(0L, taiInstant1.getTaiSeconds());
        assertFalse(boolean0);
        assertEquals(0, taiInstant1.getNano());
    }
}
