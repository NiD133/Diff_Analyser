package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest22 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        Seconds seconds0 = Seconds.seconds(352831696);
        Seconds seconds1 = seconds0.plus(3058);
        Seconds seconds2 = seconds0.minus(seconds1);
        assertEquals(352834754, seconds1.getSeconds());
        assertEquals((-3058), seconds2.getSeconds());
    }
}
