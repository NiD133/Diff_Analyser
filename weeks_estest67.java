package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest67 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test66() throws Throwable {
        Weeks weeks0 = Weeks.TWO;
        PeriodType periodType0 = weeks0.getPeriodType();
        assertEquals(1, periodType0.size());
    }
}
