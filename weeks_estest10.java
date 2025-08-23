package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest10 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        Seconds seconds0 = Seconds.MAX_VALUE;
        Weeks weeks0 = seconds0.toStandardWeeks();
        Hours hours0 = weeks0.toStandardHours();
        assertEquals(596400, hours0.getHours());
    }
}
