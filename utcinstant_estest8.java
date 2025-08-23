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

public class UtcInstant_ESTestTest8 extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        UtcInstant utcInstant0 = UtcInstant.parse("1958-01-01T00:53:27.000001Z");
        assertEquals(3207000001000L, utcInstant0.getNanoOfDay());
        assertEquals(36204L, utcInstant0.getModifiedJulianDay());
    }
}
