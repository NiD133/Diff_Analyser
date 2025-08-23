package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest63 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test62() throws Throwable {
        Seconds seconds0 = Seconds.MIN_VALUE;
        Minutes minutes0 = seconds0.toStandardMinutes();
        Seconds seconds1 = minutes0.toStandardSeconds();
        assertEquals((-35791394), minutes0.getMinutes());
        assertEquals((-2147483640), seconds1.getSeconds());
    }
}