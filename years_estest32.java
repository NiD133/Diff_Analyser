package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest32 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        Years years0 = Years.years((-690));
        boolean boolean0 = years0.isGreaterThan(years0);
        assertEquals((-690), years0.getYears());
        assertFalse(boolean0);
    }
}
