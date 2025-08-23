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

public class TaiInstant_ESTestTest52 extends TaiInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test51() throws Throwable {
        TaiInstant taiInstant0 = TaiInstant.ofTaiSeconds(124934400L, (-1909L));
        TaiInstant taiInstant1 = taiInstant0.withNano(331);
        boolean boolean0 = taiInstant0.equals(taiInstant1);
        assertFalse(taiInstant1.equals((Object) taiInstant0));
        assertFalse(boolean0);
        assertEquals(124934399L, taiInstant0.getTaiSeconds());
        assertEquals(331, taiInstant1.getNano());
        assertEquals(999998091, taiInstant0.getNano());
        assertEquals(124934399L, taiInstant1.getTaiSeconds());
    }
}