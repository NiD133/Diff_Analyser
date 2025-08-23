package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest39 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        Seconds seconds0 = Seconds.MAX_VALUE;
        boolean boolean0 = seconds0.isLessThan((Seconds) null);
        assertFalse(boolean0);
    }
}
