package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest12 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        Days days0 = Days.FIVE;
        Seconds seconds0 = days0.toStandardSeconds();
        Days days1 = seconds0.toStandardDays();
        assertEquals(432000, seconds0.getSeconds());
        assertEquals(5, days1.getDays());
    }
}
