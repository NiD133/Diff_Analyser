package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest8 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        Minutes minutes0 = Minutes.ONE;
        Seconds seconds0 = minutes0.toStandardSeconds();
        assertEquals(60, seconds0.getSeconds());
    }
}
