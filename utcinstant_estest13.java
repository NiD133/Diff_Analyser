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

public class UtcInstant_ESTestTest13 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        TaiInstant taiInstant0 = TaiInstant.ofTaiSeconds(3217L, 1000L);
        TaiInstant taiInstant1 = taiInstant0.withTaiSeconds(0L);
        UtcInstant utcInstant0 = UtcInstant.of(taiInstant1);
        TaiInstant taiInstant2 = utcInstant0.toTaiInstant();
        assertEquals(0L, taiInstant2.getTaiSeconds());
    }
}
