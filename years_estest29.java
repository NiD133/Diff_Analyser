package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest29 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        Years years0 = Years.THREE;
        Years years1 = Years.years((-1));
        boolean boolean0 = years0.isGreaterThan(years1);
        assertEquals((-1), years1.getYears());
        assertTrue(boolean0);
    }
}
