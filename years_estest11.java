package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest11 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        Years years0 = Years.years((-1249));
        Years years1 = years0.multipliedBy((-1249));
        assertEquals((-1249), years0.getYears());
        assertEquals(1560001, years1.getYears());
    }
}
