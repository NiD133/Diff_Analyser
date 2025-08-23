package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest68 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test67() throws Throwable {
        Seconds seconds0 = Seconds.ONE;
        PeriodType periodType0 = seconds0.getPeriodType();
        assertEquals(1, periodType0.size());
    }
}
