package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest42 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test41() throws Throwable {
        Seconds seconds0 = Seconds.seconds(2);
        Seconds seconds1 = Seconds.MIN_VALUE;
        boolean boolean0 = seconds0.isGreaterThan(seconds1);
        assertEquals(2, seconds0.getSeconds());
        assertTrue(boolean0);
    }
}
