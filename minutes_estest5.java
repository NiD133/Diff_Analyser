package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest5 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        Minutes minutes0 = Minutes.MAX_VALUE;
        Weeks weeks0 = minutes0.toStandardWeeks();
        assertEquals(213044, weeks0.getWeeks());
    }
}
