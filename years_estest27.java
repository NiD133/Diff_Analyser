package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest27 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        Years years0 = Years.years(0);
        boolean boolean0 = years0.isLessThan((Years) null);
        assertFalse(boolean0);
        assertEquals(0, years0.getYears());
    }
}
