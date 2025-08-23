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

public class UtcInstant_ESTestTest11 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        UtcInstant utcInstant0 = UtcInstant.ofModifiedJulianDay((-2985L), 0L);
        UtcInstant utcInstant1 = utcInstant0.withNanoOfDay(1000L);
        assertEquals(1000L, utcInstant1.getNanoOfDay());
        assertEquals((-2985L), utcInstant0.getModifiedJulianDay());
        assertEquals((-2985L), utcInstant1.getModifiedJulianDay());
    }
}
