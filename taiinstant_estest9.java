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

public class TaiInstant_ESTestTest9 extends TaiInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        TaiInstant taiInstant0 = TaiInstant.ofTaiSeconds(50L, 50L);
        // Undeclared exception!
        try {
            taiInstant0.withNano(1000000000);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // NanoOfSecond must be from 0 to 999,999,999
            //
            verifyException("org.threeten.extra.scale.TaiInstant", e);
        }
    }
}
