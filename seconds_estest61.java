package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest61 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test60() throws Throwable {
        Seconds seconds0 = Seconds.MIN_VALUE;
        Hours hours0 = seconds0.toStandardHours();
        assertEquals((-596523), hours0.getHours());
    }
}
