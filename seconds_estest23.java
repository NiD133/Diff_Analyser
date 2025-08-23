package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest23 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        Seconds seconds0 = Seconds.seconds(0);
        Seconds seconds1 = seconds0.minus(0);
        assertEquals(0, seconds1.getSeconds());
    }
}
