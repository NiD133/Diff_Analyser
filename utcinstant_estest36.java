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

public class UtcInstant_ESTestTest36 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test35() throws Throwable {
        // Undeclared exception!
        try {
            UtcInstant.ofModifiedJulianDay((-361L), (-361L));
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            //
            // Nanosecond-of-day must be between 0 and 86400000000000 on date -361
            //
            verifyException("org.threeten.extra.scale.UtcRules", e);
        }
    }
}
