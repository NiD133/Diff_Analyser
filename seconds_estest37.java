package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest37 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test36() throws Throwable {
        Days days0 = Days.ZERO;
        Seconds seconds0 = days0.toStandardSeconds();
        Seconds seconds1 = seconds0.minus(1);
        assertEquals((-1), seconds1.getSeconds());
    }
}
