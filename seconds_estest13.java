package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest13 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        Weeks weeks0 = Weeks.TWO;
        Days days0 = weeks0.toStandardDays();
        Seconds seconds0 = days0.toStandardSeconds();
        Seconds seconds1 = seconds0.plus((-2147138048));
        Days days1 = seconds1.toStandardDays();
        assertEquals(1209600, seconds0.getSeconds());
        assertEquals((-2145928448), seconds1.getSeconds());
        assertEquals((-24837), days1.getDays());
    }
}
