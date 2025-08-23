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

public class UtcInstant_ESTestTest28 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        TaiInstant taiInstant0 = TaiInstant.ofTaiSeconds(3217L, 1000L);
        UtcInstant utcInstant0 = taiInstant0.toUtcInstant();
        Instant instant0 = MockInstant.ofEpochMilli(3217L);
        UtcInstant utcInstant1 = UtcInstant.of(instant0);
        int int0 = utcInstant0.compareTo(utcInstant1);
        assertEquals(3217000000L, utcInstant1.getNanoOfDay());
        assertEquals((-1), int0);
    }
}
