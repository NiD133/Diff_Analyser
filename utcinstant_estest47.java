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

public class UtcInstant_ESTestTest47 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test46() throws Throwable {
        UtcInstant utcInstant0 = UtcInstant.ofModifiedJulianDay(0L, 0L);
        String string0 = utcInstant0.toString();
        assertEquals("1858-11-17T00:00:00Z", string0);
    }
}
