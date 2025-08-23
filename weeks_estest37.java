package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest37 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test36() throws Throwable {
        Weeks weeks0 = Weeks.ZERO;
        Seconds seconds0 = weeks0.ONE.toStandardSeconds();
        Weeks weeks1 = seconds0.toStandardWeeks();
        assertEquals(1, weeks1.getWeeks());
        assertEquals(604800, seconds0.getSeconds());
    }
}