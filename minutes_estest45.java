package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest45 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test44() throws Throwable {
        Minutes minutes0 = Minutes.MIN_VALUE;
        boolean boolean0 = minutes0.isLessThan((Minutes) null);
        assertTrue(boolean0);
    }
}
