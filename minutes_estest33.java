package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest33 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        Days days0 = Days.THREE;
        Minutes minutes0 = Minutes.standardMinutesIn(days0);
        assertEquals(4320, minutes0.getMinutes());
    }
}
