package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest24 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        Minutes minutes0 = Minutes.ZERO;
        Weeks weeks0 = minutes0.toStandardWeeks();
        int int0 = weeks0.getWeeks();
        assertEquals(0, int0);
    }
}
