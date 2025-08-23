package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest16 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        Weeks weeks0 = Weeks.MIN_VALUE;
        Weeks weeks1 = weeks0.plus(4);
        assertEquals((-2147483644), weeks1.getWeeks());
    }
}
