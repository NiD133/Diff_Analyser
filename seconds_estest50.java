package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest50 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test49() throws Throwable {
        Seconds seconds0 = Seconds.parseSeconds((String) null);
        assertEquals(1, seconds0.size());
    }
}
