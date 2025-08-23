package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest11 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        Seconds seconds0 = Seconds.MIN_VALUE;
        Duration duration0 = seconds0.toStandardDuration();
        assertEquals((-2147483648000L), duration0.getMillis());
    }
}
