package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest42 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test41() throws Throwable {
        Years years0 = Years.years(0);
        Years years1 = years0.TWO.plus(2);
        boolean boolean0 = years0.isLessThan(years1);
        assertEquals(4, years1.getYears());
        assertTrue(boolean0);
    }
}
