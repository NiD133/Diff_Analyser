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

public class TaiInstant_ESTestTest22 extends TaiInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        Instant instant0 = MockInstant.now();
        TaiInstant taiInstant0 = TaiInstant.of(instant0);
        Duration duration0 = Duration.ZERO;
        TaiInstant taiInstant1 = taiInstant0.withTaiSeconds((-2L));
        TaiInstant taiInstant2 = taiInstant1.minus(duration0);
        assertEquals((-2L), taiInstant2.getTaiSeconds());
        assertSame(taiInstant2, taiInstant1);
        assertEquals(320000000, taiInstant0.getNano());
        assertEquals(320000000, taiInstant2.getNano());
    }
}
