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

public class TaiInstant_ESTestTest26 extends TaiInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        TaiInstant taiInstant0 = TaiInstant.ofTaiSeconds(1L, 0L);
        int int0 = taiInstant0.getNano();
        assertEquals(1L, taiInstant0.getTaiSeconds());
        assertEquals(0, int0);
    }
}
