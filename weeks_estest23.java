package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest23 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        Weeks weeks0 = Weeks.weeks(317351877);
        Weeks weeks1 = weeks0.minus(2285);
        assertEquals(317349592, weeks1.getWeeks());
    }
}
