package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest61 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test60() throws Throwable {
        Minutes minutes0 = Minutes.minutes(Integer.MIN_VALUE);
        Hours hours0 = minutes0.toStandardHours();
        assertEquals(Integer.MIN_VALUE, minutes0.getMinutes());
        assertEquals((-35791394), hours0.getHours());
    }
}
