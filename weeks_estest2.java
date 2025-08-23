package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest2 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        Minutes minutes0 = Minutes.THREE;
        Seconds seconds0 = minutes0.toStandardSeconds();
        Weeks weeks0 = seconds0.toStandardWeeks();
        boolean boolean0 = weeks0.isLessThan((Weeks) null);
        assertFalse(boolean0);
        assertEquals(0, weeks0.getWeeks());
    }
}
