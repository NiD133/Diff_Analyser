package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest12 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        Weeks weeks0 = Weeks.ZERO;
        Duration duration0 = weeks0.TWO.toStandardDuration();
        assertEquals(1209600000L, duration0.getMillis());
    }
}
