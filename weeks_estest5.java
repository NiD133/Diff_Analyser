package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest5 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        Weeks weeks0 = Weeks.weeks((-2820));
        Seconds seconds0 = weeks0.toStandardSeconds();
        assertEquals((-1705536000), seconds0.getSeconds());
    }
}
