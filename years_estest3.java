package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest3 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Years years0 = Years.years((-690));
        Years years1 = Years.MAX_VALUE;
        boolean boolean0 = years0.isGreaterThan(years1);
        assertEquals((-690), years0.getYears());
        assertFalse(boolean0);
    }
}
