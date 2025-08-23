package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest1 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Minutes minutes0 = Minutes.TWO;
        Minutes minutes1 = Minutes.MAX_VALUE;
        boolean boolean0 = minutes1.isLessThan(minutes0);
        assertFalse(boolean0);
    }
}
