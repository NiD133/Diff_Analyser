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

public class UtcInstant_ESTestTest15 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        UtcInstant utcInstant0 = UtcInstant.ofModifiedJulianDay(296L, 0L);
        TaiInstant taiInstant0 = utcInstant0.toTaiInstant();
        assertEquals(0, taiInstant0.getNano());
        assertEquals((-3102451190L), taiInstant0.getTaiSeconds());
    }
}
