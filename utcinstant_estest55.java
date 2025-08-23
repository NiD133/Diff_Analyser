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

public class UtcInstant_ESTestTest55 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test54() throws Throwable {
        UtcInstant utcInstant0 = UtcInstant.ofModifiedJulianDay(83843999999988L, 83843999999988L);
        assertEquals(83843999999988L, utcInstant0.getNanoOfDay());
        UtcInstant utcInstant1 = utcInstant0.withModifiedJulianDay((-1406L));
        boolean boolean0 = utcInstant0.isAfter(utcInstant1);
        assertEquals(83843999999988L, utcInstant1.getNanoOfDay());
        assertEquals((-1406L), utcInstant1.getModifiedJulianDay());
        assertTrue(boolean0);
    }
}
