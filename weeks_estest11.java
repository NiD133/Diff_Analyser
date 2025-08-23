package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest11 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        Weeks weeks0 = Weeks.ZERO;
        Weeks weeks1 = Weeks.THREE;
        Weeks weeks2 = weeks0.minus(weeks1);
        Hours hours0 = weeks2.toStandardHours();
        assertEquals((-504), hours0.getHours());
    }
}
