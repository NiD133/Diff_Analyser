package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest17 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        Weeks weeks0 = Weeks.ZERO;
        Weeks weeks1 = weeks0.negated();
        assertEquals(0, weeks1.getWeeks());
    }
}
