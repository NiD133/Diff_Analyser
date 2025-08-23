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

public class TaiInstant_ESTestTest43 extends TaiInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test42() throws Throwable {
        TaiInstant taiInstant0 = TaiInstant.ofTaiSeconds(0L, 0L);
        TaiInstant taiInstant1 = taiInstant0.withTaiSeconds(5495L);
        boolean boolean0 = taiInstant1.equals(taiInstant0);
        assertEquals(5495L, taiInstant1.getTaiSeconds());
        assertFalse(taiInstant0.equals((Object) taiInstant1));
        assertEquals(0, taiInstant1.getNano());
        assertFalse(boolean0);
    }
}
