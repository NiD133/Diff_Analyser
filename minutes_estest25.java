package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest25 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        Minutes minutes0 = Minutes.ZERO;
        Minutes minutes1 = minutes0.minus(0);
        assertEquals(0, minutes1.getMinutes());
    }
}
