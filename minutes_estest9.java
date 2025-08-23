package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest9 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        Minutes minutes0 = Minutes.minutes(1431);
        Hours hours0 = minutes0.toStandardHours();
        assertEquals(1431, minutes0.getMinutes());
        assertEquals(23, hours0.getHours());
    }
}
