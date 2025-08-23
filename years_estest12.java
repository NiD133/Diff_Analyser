package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest12 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        Years years0 = Years.THREE;
        Years years1 = years0.multipliedBy((-1129));
        assertEquals((-3387), years1.getYears());
    }
}
