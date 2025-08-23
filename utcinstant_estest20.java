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

public class UtcInstant_ESTestTest20 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        UtcInstant utcInstant0 = UtcInstant.ofModifiedJulianDay(0L, 0L);
        TaiInstant taiInstant0 = TaiInstant.of(utcInstant0);
        UtcInstant utcInstant1 = UtcInstant.of(taiInstant0);
        assertEquals(0L, utcInstant1.getNanoOfDay());
    }
}
