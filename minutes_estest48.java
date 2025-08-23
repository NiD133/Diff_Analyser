package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest48 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test47() throws Throwable {
        Minutes minutes0 = Minutes.MIN_VALUE;
        boolean boolean0 = minutes0.isGreaterThan((Minutes) null);
        assertFalse(boolean0);
    }
}
