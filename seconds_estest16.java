package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest16 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        Seconds seconds0 = Seconds.seconds((-2530));
        Seconds seconds1 = seconds0.plus(seconds0);
        assertEquals((-5060), seconds1.getSeconds());
        assertEquals((-2530), seconds0.getSeconds());
    }
}
