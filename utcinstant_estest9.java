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

public class UtcInstant_ESTestTest9 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        Instant instant0 = MockInstant.ofEpochSecond(0L);
        UtcInstant utcInstant0 = UtcInstant.of(instant0);
        UtcInstant utcInstant1 = utcInstant0.withNanoOfDay(0L);
        assertEquals(0L, utcInstant1.getNanoOfDay());
        assertEquals(40587L, utcInstant0.getModifiedJulianDay());
        assertTrue(utcInstant1.equals((Object) utcInstant0));
    }
}
