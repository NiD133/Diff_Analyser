package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest26 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        Years years0 = Years.MIN_VALUE;
        boolean boolean0 = years0.isLessThan((Years) null);
        assertTrue(boolean0);
    }
}
