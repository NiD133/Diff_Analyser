package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest16 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        Years years0 = Years.years(0);
        Years years1 = years0.TWO.plus(2);
        int int0 = years1.getYears();
        assertEquals(4, int0);
    }
}
