package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest47 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test46() throws Throwable {
        Years years0 = Years.years(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, years0.getYears());
    }
}
