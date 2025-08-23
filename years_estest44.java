package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest44 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test43() throws Throwable {
        Years years0 = Years.years(3);
        assertEquals(3, years0.getYears());
    }
}
