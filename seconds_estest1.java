package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest1 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Hours hours0 = Hours.ONE;
        Days days0 = hours0.toStandardDays();
        Seconds seconds0 = days0.toStandardSeconds();
        boolean boolean0 = seconds0.isLessThan((Seconds) null);
        assertFalse(boolean0);
        assertEquals(0, seconds0.getSeconds());
    }
}
