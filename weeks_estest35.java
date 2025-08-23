package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest35 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test34() throws Throwable {
        Weeks weeks0 = Weeks.weeks(0);
        Weeks weeks1 = weeks0.minus(0);
        assertEquals(0, weeks1.getWeeks());
    }
}