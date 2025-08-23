package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest6 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Years years0 = Years.MIN_VALUE;
        Years years1 = Years.TWO;
        Years years2 = years1.plus(years0);
        assertEquals((-2147483646), years2.getYears());
    }
}
