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

public class UtcInstant_ESTestTest17 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        Duration duration0 = Duration.ofNanos((-1606L));
        UtcInstant utcInstant0 = UtcInstant.ofModifiedJulianDay((-571L), 0L);
        UtcInstant utcInstant1 = utcInstant0.plus(duration0);
        assertEquals((-572L), utcInstant1.getModifiedJulianDay());
        assertEquals(86399999998394L, utcInstant1.getNanoOfDay());
    }
}
