package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest44 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test43() throws Throwable {
        Seconds seconds0 = Seconds.seconds((-2147483646));
        boolean boolean0 = seconds0.isGreaterThan((Seconds) null);
        assertFalse(boolean0);
        assertEquals((-2147483646), seconds0.getSeconds());
    }
}
