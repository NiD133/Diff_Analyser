package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest59 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test58() throws Throwable {
        Seconds seconds0 = Seconds.TWO;
        Days days0 = seconds0.toStandardDays();
        assertEquals(0, days0.getDays());
    }
}
