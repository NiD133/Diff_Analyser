package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest64 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test63() throws Throwable {
        Instant instant0 = new Instant();
        Seconds seconds0 = Seconds.secondsBetween((ReadableInstant) instant0, (ReadableInstant) instant0);
        assertEquals(0, seconds0.getSeconds());
    }
}
