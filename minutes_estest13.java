package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest13 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        Minutes minutes0 = Minutes.minutesIn((ReadableInterval) null);
        Days days0 = minutes0.MAX_VALUE.toStandardDays();
        assertEquals(1491308, days0.getDays());
    }
}
