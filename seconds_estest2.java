package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest2 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        Seconds seconds0 = Seconds.seconds(352831696);
        Seconds seconds1 = Seconds.MIN_VALUE;
        boolean boolean0 = seconds1.isGreaterThan(seconds0);
        assertFalse(boolean0);
        assertEquals(352831696, seconds0.getSeconds());
    }
}