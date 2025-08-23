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

public class UtcInstant_ESTestTest33 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        UtcInstant utcInstant0 = UtcInstant.ofModifiedJulianDay(86399999999628L, 86399999999628L);
        Duration duration0 = Duration.ofDays(86399999999628L);
        // Undeclared exception!
        try {
            utcInstant0.plus(duration0);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // long overflow
            //
            verifyException("java.lang.Math", e);
        }
    }
}
