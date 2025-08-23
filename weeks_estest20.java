package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest20 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        Weeks weeks0 = Weeks.ONE;
        Weeks weeks1 = weeks0.multipliedBy(604800);
        assertEquals(604800, weeks1.getWeeks());
    }
}
