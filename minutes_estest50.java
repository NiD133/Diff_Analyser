package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest50 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test49() throws Throwable {
        Minutes minutes0 = Minutes.ONE;
        boolean boolean0 = minutes0.isGreaterThan(minutes0);
        assertFalse(boolean0);
    }
}
