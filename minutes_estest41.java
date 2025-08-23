package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest41 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        Minutes minutes0 = Minutes.MAX_VALUE;
        Minutes minutes1 = minutes0.plus(0);
        assertEquals(Integer.MAX_VALUE, minutes1.getMinutes());
    }
}
