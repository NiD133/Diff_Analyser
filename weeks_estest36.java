package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest36 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test35() throws Throwable {
        Weeks weeks0 = Weeks.weeks(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, weeks0.getWeeks());
    }
}
