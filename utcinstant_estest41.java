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

public class UtcInstant_ESTestTest41 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        UtcInstant utcInstant0 = UtcInstant.ofModifiedJulianDay(2774999999977L, 2774999999977L);
        // Undeclared exception!
        try {
            utcInstant0.isBefore((UtcInstant) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.threeten.extra.scale.UtcInstant", e);
        }
    }
}
