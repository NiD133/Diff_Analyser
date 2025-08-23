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

public class TaiInstant_ESTestTest7 extends TaiInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Instant instant0 = MockInstant.ofEpochSecond(36204L, (-1L));
        TaiInstant taiInstant0 = TaiInstant.of(instant0);
        Duration duration0 = Duration.ofMillis((-1L));
        TaiInstant taiInstant1 = taiInstant0.minus(duration0);
        assertEquals(378727414L, taiInstant1.getTaiSeconds());
        assertEquals(999999, taiInstant1.getNano());
    }
}
