package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest70 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test69() throws Throwable {
        Instant instant0 = Instant.EPOCH;
        Weeks weeks0 = Weeks.weeksBetween((ReadableInstant) instant0, (ReadableInstant) instant0);
        assertEquals(0, weeks0.getWeeks());
    }
}
