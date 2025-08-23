package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest62 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test61() throws Throwable {
        Seconds seconds0 = Seconds.TWO;
        Seconds seconds1 = seconds0.minus(seconds0);
        assertEquals(0, seconds1.getSeconds());
    }
}
