package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest52 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test51() throws Throwable {
        Years years0 = Years.years(0);
        int int0 = years0.getYears();
        assertEquals(0, int0);
    }
}
