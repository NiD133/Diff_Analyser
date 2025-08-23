package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest4 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        Weeks weeks0 = Weeks.MIN_VALUE;
        boolean boolean0 = weeks0.isGreaterThan((Weeks) null);
        assertFalse(boolean0);
    }
}
