package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest70 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test69() throws Throwable {
        Seconds seconds0 = Seconds.MAX_VALUE;
        Duration duration0 = seconds0.toStandardDuration();
        assertEquals(24855L, duration0.getStandardDays());
    }
}
