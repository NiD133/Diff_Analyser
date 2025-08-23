package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest28 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        Seconds seconds0 = Seconds.THREE;
        Seconds seconds1 = seconds0.MIN_VALUE.dividedBy(3600);
        assertEquals((-596523), seconds1.getSeconds());
    }
}
