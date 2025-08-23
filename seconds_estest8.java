package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest8 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        Seconds seconds0 = Seconds.ZERO;
        Hours hours0 = seconds0.toStandardHours();
        assertEquals(0, hours0.getHours());
    }
}
