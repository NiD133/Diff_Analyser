package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest14 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        Weeks weeks0 = Weeks.ZERO;
        Days days0 = weeks0.THREE.toStandardDays();
        assertEquals(21, days0.getDays());
    }
}
