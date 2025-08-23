package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest24 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        Seconds seconds0 = Seconds.seconds(352831696);
        Seconds seconds1 = seconds0.MAX_VALUE.minus(352831696);
        assertEquals(1794651951, seconds1.getSeconds());
    }
}
