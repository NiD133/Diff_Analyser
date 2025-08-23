package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest55 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test54() throws Throwable {
        Weeks weeks0 = Weeks.weeks(3);
        assertEquals(3, weeks0.getWeeks());
    }
}
