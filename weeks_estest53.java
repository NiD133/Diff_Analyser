package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest53 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test52() throws Throwable {
        Weeks weeks0 = Weeks.weeksIn((ReadableInterval) null);
        Weeks weeks1 = weeks0.minus((-2358));
        Weeks weeks2 = weeks1.negated();
        Days days0 = weeks2.toStandardDays();
        assertEquals(2358, weeks1.getWeeks());
        assertEquals((-16506), days0.getDays());
    }
}