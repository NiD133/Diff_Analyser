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

public class TaiInstant_ESTestTest53 extends TaiInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test52() throws Throwable {
        TaiInstant taiInstant0 = TaiInstant.ofTaiSeconds((-604800L), (-604800L));
        // Undeclared exception!
        try {
            taiInstant0.withNano(2147424333);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // NanoOfSecond must be from 0 to 999,999,999
            //
            verifyException("org.threeten.extra.scale.TaiInstant", e);
        }
    }
}
