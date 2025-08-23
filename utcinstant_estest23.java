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

public class UtcInstant_ESTestTest23 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        UtcInstant utcInstant0 = UtcInstant.ofModifiedJulianDay(140L, 140L);
        Duration duration0 = Duration.ofMinutes(140L);
        UtcInstant utcInstant1 = utcInstant0.minus(duration0);
        assertEquals(139L, utcInstant1.getModifiedJulianDay());
        assertEquals(78000000000140L, utcInstant1.getNanoOfDay());
    }
}
