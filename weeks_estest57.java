package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest57 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test56() throws Throwable {
        Weeks weeks0 = Weeks.THREE;
        Weeks weeks1 = Weeks.TWO;
        Weeks weeks2 = weeks0.minus(weeks1);
        assertEquals(1, weeks2.getWeeks());
    }
}