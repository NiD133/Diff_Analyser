package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest17 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        Years years0 = Years.years((-690));
        int int0 = years0.getYears();
        assertEquals((-690), int0);
    }
}
