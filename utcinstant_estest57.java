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

public class UtcInstant_ESTestTest57 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test56() throws Throwable {
        UtcInstant utcInstant0 = UtcInstant.ofModifiedJulianDay(83843999999988L, 83843999999988L);
        boolean boolean0 = utcInstant0.isLeapSecond();
        assertEquals(83843999999988L, utcInstant0.getModifiedJulianDay());
        assertFalse(boolean0);
        assertEquals(83843999999988L, utcInstant0.getNanoOfDay());
    }
}
