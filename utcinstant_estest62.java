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

public class UtcInstant_ESTestTest62 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test61() throws Throwable {
        UtcInstant utcInstant0 = UtcInstant.ofModifiedJulianDay(73281320003518L, 73281320003518L);
        Duration duration0 = Duration.ofMillis(73281320003518L);
        UtcInstant utcInstant1 = utcInstant0.plus(duration0);
        boolean boolean0 = utcInstant0.isBefore(utcInstant1);
        assertEquals(73281320003518L, utcInstant0.getModifiedJulianDay());
        assertTrue(boolean0);
        assertEquals(23684838003518L, utcInstant1.getNanoOfDay());
    }
}
