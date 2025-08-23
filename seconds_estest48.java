package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest48 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test47() throws Throwable {
        Seconds seconds0 = Seconds.ONE;
        Seconds seconds1 = seconds0.plus((Seconds) null);
        assertEquals(1, seconds1.getSeconds());
    }
}
