package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest19 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        Years years0 = Years.MAX_VALUE;
        Years years1 = years0.dividedBy((-592));
        assertEquals((-3627506), years1.getYears());
    }
}
