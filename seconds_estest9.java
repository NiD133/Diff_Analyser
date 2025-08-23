package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest9 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        Seconds seconds0 = Seconds.seconds(690562340);
        Hours hours0 = seconds0.toStandardHours();
        assertEquals(191822, hours0.getHours());
        assertEquals(690562340, seconds0.getSeconds());
    }
}
