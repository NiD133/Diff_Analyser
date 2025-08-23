package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest55 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test54() throws Throwable {
        Years years0 = Years.TWO;
        String string0 = years0.toString();
        assertEquals("P2Y", string0);
    }
}
