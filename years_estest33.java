package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest33 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        Years years0 = Years.MAX_VALUE;
        Years years1 = years0.dividedBy(1);
        assertEquals(Integer.MAX_VALUE, years1.getYears());
    }
}
