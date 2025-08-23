package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest13 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        Years years0 = Years.TWO;
        Years years1 = years0.plus((-1131));
        Years years2 = years1.minus(years0);
        assertEquals((-1131), years2.getYears());
    }
}
