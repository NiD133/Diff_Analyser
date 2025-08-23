package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest36 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test35() throws Throwable {
        Years years0 = Years.THREE;
        Years years1 = years0.minus(years0);
        assertEquals(0, years1.getYears());
    }
}
