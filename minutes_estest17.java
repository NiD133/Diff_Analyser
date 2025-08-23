package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest17 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        Minutes minutes0 = Minutes.ZERO;
        Minutes minutes1 = minutes0.plus((-2104));
        assertEquals((-2104), minutes1.getMinutes());
    }
}
