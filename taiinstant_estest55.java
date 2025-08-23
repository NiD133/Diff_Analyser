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

public class TaiInstant_ESTestTest55 extends TaiInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test54() throws Throwable {
        // Undeclared exception!
        try {
            TaiInstant.parse("0.000000000s(TAI)");
            //  fail("Expecting exception: IllegalStateException");
            // Unstable assertion
        } catch (IllegalStateException e) {
            //
            // No match found
            //
            verifyException("java.util.regex.Matcher", e);
        }
    }
}
