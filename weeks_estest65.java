package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest65 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test64() throws Throwable {
        Weeks weeks0 = Weeks.THREE;
        int int0 = weeks0.getWeeks();
        assertEquals(3, int0);
    }
}
