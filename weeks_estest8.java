package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest8 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        Weeks weeks0 = Weeks.weeksIn((ReadableInterval) null);
        Weeks weeks1 = weeks0.minus(3765);
        Minutes minutes0 = weeks1.toStandardMinutes();
        assertEquals((-37951200), minutes0.getMinutes());
    }
}
