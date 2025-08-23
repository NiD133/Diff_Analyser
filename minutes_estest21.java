package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest21 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        Minutes minutes0 = Minutes.minutes(1431);
        Minutes minutes1 = minutes0.multipliedBy(3012);
        assertEquals(1431, minutes0.getMinutes());
        assertEquals(4310172, minutes1.getMinutes());
    }
}