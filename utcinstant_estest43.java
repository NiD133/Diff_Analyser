package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.time.MockInstant;
import org.junit.runner.RunWith;

public class UtcInstant_ESTestTest43 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test42() throws Throwable {
        UtcInstant utcInstant0 = UtcInstant.ofModifiedJulianDay(1000000000L, 1000000000L);
        // Undeclared exception!
        try {
            utcInstant0.durationUntil((UtcInstant) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.threeten.extra.scale.UtcInstant", e);
        }
    }
}
