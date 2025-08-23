package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest13 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        Weeks weeks0 = Weeks.weeksIn((ReadableInterval) null);
        Days days0 = weeks0.toStandardDays();
        assertEquals(1, days0.size());
    }
}
