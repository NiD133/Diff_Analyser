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

public class UtcInstant_ESTestTest6 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        TaiInstant taiInstant0 = TaiInstant.ofTaiSeconds(3217L, 1000L);
        UtcInstant utcInstant0 = taiInstant0.toUtcInstant();
        TaiInstant taiInstant1 = taiInstant0.withTaiSeconds(0L);
        UtcInstant utcInstant1 = UtcInstant.of(taiInstant1);
        boolean boolean0 = utcInstant1.isAfter(utcInstant0);
        assertFalse(boolean0);
        assertEquals(86390000001000L, utcInstant1.getNanoOfDay());
    }
}
