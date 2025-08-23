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

public class UtcInstant_ESTestTest22 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        UtcInstant utcInstant0 = UtcInstant.ofModifiedJulianDay((-127L), 41317L);
        Instant instant0 = utcInstant0.toInstant();
        UtcInstant utcInstant1 = UtcInstant.of(instant0);
        assertTrue(utcInstant1.equals((Object) utcInstant0));
        assertEquals((-127L), utcInstant1.getModifiedJulianDay());
    }
}
