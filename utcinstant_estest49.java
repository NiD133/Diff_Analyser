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

public class UtcInstant_ESTestTest49 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test48() throws Throwable {
        UtcInstant utcInstant0 = UtcInstant.ofModifiedJulianDay(0L, 0L);
        Duration duration0 = Duration.ofSeconds(1526L);
        UtcInstant utcInstant1 = utcInstant0.minus(duration0);
        boolean boolean0 = utcInstant0.equals(utcInstant1);
        assertEquals(84874000000000L, utcInstant1.getNanoOfDay());
        assertEquals((-1L), utcInstant1.getModifiedJulianDay());
        assertFalse(boolean0);
    }
}
