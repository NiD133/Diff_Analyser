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

public class UtcInstant_ESTestTest25 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        UtcInstant utcInstant0 = UtcInstant.ofModifiedJulianDay(54L, 54L);
        long long0 = utcInstant0.getNanoOfDay();
        assertEquals(54L, long0);
        assertEquals(54L, utcInstant0.getModifiedJulianDay());
    }
}
