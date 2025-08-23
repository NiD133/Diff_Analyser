package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest47 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test46() throws Throwable {
        Weeks weeks0 = Weeks.MAX_VALUE;
        Weeks weeks1 = weeks0.dividedBy((-1));
        assertEquals((-2147483647), weeks1.getWeeks());
    }
}
