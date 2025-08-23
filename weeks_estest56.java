package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest56 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test55() throws Throwable {
        Weeks weeks0 = Weeks.weeks(2);
        Weeks weeks1 = weeks0.plus(2);
        assertEquals(4, weeks1.getWeeks());
    }
}
