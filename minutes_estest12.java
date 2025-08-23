package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest12 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        Minutes minutes0 = Minutes.MIN_VALUE;
        Duration duration0 = minutes0.toStandardDuration();
        assertEquals((-2147483648L), duration0.getStandardMinutes());
    }
}
