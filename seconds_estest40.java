package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest40 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test39() throws Throwable {
        Seconds seconds0 = Seconds.seconds((-1300));
        boolean boolean0 = seconds0.isLessThan((Seconds) null);
        assertTrue(boolean0);
        assertEquals((-1300), seconds0.getSeconds());
    }
}
