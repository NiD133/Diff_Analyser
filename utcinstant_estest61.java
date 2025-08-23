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

public class UtcInstant_ESTestTest61 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test60() throws Throwable {
        UtcInstant utcInstant0 = UtcInstant.ofModifiedJulianDay(86340000001821L, 86340000001821L);
        // Undeclared exception!
        try {
            utcInstant0.withNanoOfDay((-892L));
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            //
            // Nanosecond-of-day must be between 0 and 86400000000000 on date 86340000001821
            //
            verifyException("org.threeten.extra.scale.UtcRules", e);
        }
    }
}
