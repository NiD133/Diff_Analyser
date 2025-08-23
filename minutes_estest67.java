package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest67 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test66() throws Throwable {
        Minutes minutes0 = Minutes.MAX_VALUE;
        String string0 = minutes0.toString();
        assertEquals("PT2147483647M", string0);
    }
}
