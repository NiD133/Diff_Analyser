package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest25 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        Weeks weeks0 = Weeks.MIN_VALUE;
        int int0 = weeks0.getWeeks();
        assertEquals(Integer.MIN_VALUE, int0);
    }
}
