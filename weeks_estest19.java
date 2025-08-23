package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest19 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        Weeks weeks0 = Weeks.parseWeeks((String) null);
        Weeks weeks1 = weeks0.multipliedBy((-1060));
        assertEquals(0, weeks1.getWeeks());
    }
}
