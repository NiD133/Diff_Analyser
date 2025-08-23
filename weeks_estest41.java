package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest41 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        Weeks weeks0 = Weeks.standardWeeksIn((ReadablePeriod) null);
        boolean boolean0 = weeks0.isLessThan(weeks0);
        assertFalse(boolean0);
        assertEquals(0, weeks0.getWeeks());
    }
}
