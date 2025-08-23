package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest26 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        Seconds seconds0 = Seconds.seconds((-2530));
        int int0 = seconds0.getSeconds();
        assertEquals((-2530), int0);
    }
}
