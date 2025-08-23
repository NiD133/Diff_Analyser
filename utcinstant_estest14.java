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

public class UtcInstant_ESTestTest14 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        TaiInstant taiInstant0 = TaiInstant.ofTaiSeconds(3217L, 1000L);
        UtcInstant utcInstant0 = taiInstant0.toUtcInstant();
        TaiInstant taiInstant1 = utcInstant0.toTaiInstant();
        assertEquals(3217L, taiInstant1.getTaiSeconds());
    }
}
