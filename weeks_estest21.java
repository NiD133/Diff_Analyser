package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest21 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        Weeks weeks0 = Weeks.THREE;
        Weeks weeks1 = weeks0.multipliedBy((-617));
        assertEquals((-1851), weeks1.getWeeks());
    }
}
