package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest31 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        Hours hours0 = Hours.MIN_VALUE;
        Weeks weeks0 = Weeks.standardWeeksIn(hours0);
        assertEquals((-12782640), weeks0.getWeeks());
    }
}
