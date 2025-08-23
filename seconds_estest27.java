package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest27 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        Seconds seconds0 = Seconds.seconds(1804);
        Seconds seconds1 = seconds0.dividedBy(1804);
        assertEquals(1804, seconds0.getSeconds());
        assertEquals(1, seconds1.getSeconds());
    }
}
