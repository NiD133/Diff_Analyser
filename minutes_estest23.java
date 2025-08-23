package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest23 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        Minutes minutes0 = Minutes.ONE;
        Minutes minutes1 = Minutes.minutes(0);
        Minutes minutes2 = minutes0.ONE.minus(minutes1);
        assertEquals(1, minutes2.getMinutes());
        assertEquals(0, minutes1.getMinutes());
    }
}
