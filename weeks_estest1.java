package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest1 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Minutes minutes0 = Minutes.ZERO;
        Weeks weeks0 = Weeks.TWO;
        Weeks weeks1 = minutes0.toStandardWeeks();
        boolean boolean0 = weeks0.isLessThan(weeks1);
        assertFalse(boolean0);
        assertEquals(0, weeks1.getWeeks());
    }
}
