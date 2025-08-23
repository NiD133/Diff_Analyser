package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest69 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test68() throws Throwable {
        Instant instant0 = Instant.now();
        Minutes minutes0 = Minutes.minutesBetween((ReadableInstant) instant0, (ReadableInstant) instant0);
        assertEquals(0, minutes0.getMinutes());
    }
}
