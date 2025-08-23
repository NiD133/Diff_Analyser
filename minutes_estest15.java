package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest15 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        Seconds seconds0 = Seconds.MAX_VALUE;
        Minutes minutes0 = seconds0.toStandardMinutes();
        Minutes minutes1 = minutes0.MAX_VALUE.dividedBy((-82));
        Minutes minutes2 = minutes1.plus(minutes1);
        assertEquals((-26188824), minutes1.getMinutes());
        assertEquals((-52377648), minutes2.getMinutes());
    }
}
