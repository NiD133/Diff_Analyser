package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest72 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test71() throws Throwable {
        Minutes minutes0 = Minutes.ZERO;
        Days days0 = minutes0.toStandardDays();
        assertEquals(0, days0.getDays());
    }
}
