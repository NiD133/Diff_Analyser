package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest5 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        Seconds seconds0 = Seconds.MIN_VALUE;
        Weeks weeks0 = seconds0.toStandardWeeks();
        Seconds seconds1 = weeks0.toStandardSeconds();
        boolean boolean0 = seconds1.isLessThan(seconds0);
        assertFalse(boolean0);
        assertEquals((-2147040000), seconds1.getSeconds());
        assertEquals((-3550), weeks0.getWeeks());
    }
}
