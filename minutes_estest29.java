package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest29 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        Seconds seconds0 = Seconds.MAX_VALUE;
        Minutes minutes0 = seconds0.toStandardMinutes();
        int int0 = minutes0.getMinutes();
        assertEquals(35791394, int0);
    }
}
