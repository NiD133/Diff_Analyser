package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest43 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test42() throws Throwable {
        Minutes minutes0 = Minutes.minutes(31);
        Minutes minutes1 = Minutes.TWO;
        boolean boolean0 = minutes1.isLessThan(minutes0);
        assertTrue(boolean0);
        assertEquals(31, minutes0.getMinutes());
    }
}
