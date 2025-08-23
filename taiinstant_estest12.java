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

public class TaiInstant_ESTestTest12 extends TaiInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        Instant instant0 = MockInstant.ofEpochSecond(36204L, (-1L));
        TaiInstant taiInstant0 = TaiInstant.of(instant0);
        TaiInstant taiInstant1 = taiInstant0.withNano(0);
        assertEquals(0, taiInstant1.getNano());
        assertEquals(378727413L, taiInstant1.getTaiSeconds());
        assertEquals(378727413L, taiInstant0.getTaiSeconds());
        assertEquals(999999999, taiInstant0.getNano());
    }
}
