package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest74 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test73() throws Throwable {
        Minutes minutes0 = Minutes.ONE;
        Weeks weeks0 = minutes0.toStandardWeeks();
        assertEquals(0, weeks0.getWeeks());
    }
}
