package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest20 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        Seconds seconds0 = Seconds.seconds(1566);
        Seconds seconds1 = seconds0.multipliedBy(518);
        assertEquals(811188, seconds1.getSeconds());
        assertEquals(1566, seconds0.getSeconds());
    }
}
