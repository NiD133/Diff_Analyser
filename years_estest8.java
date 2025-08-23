package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest8 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        Years years0 = Years.ZERO;
        Years years1 = years0.minus(1612);
        years1.negated();
        assertEquals((-1612), years1.getYears());
    }
}
