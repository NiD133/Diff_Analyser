package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest7 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Duration duration0 = new Duration((-1L), (-1L));
        Days days0 = duration0.toStandardDays();
        Seconds seconds0 = days0.toStandardSeconds();
        Minutes minutes0 = seconds0.MIN_VALUE.toStandardMinutes();
        assertEquals(0, seconds0.getSeconds());
        assertEquals((-35791394), minutes0.getMinutes());
    }
}
