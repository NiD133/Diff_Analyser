package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest24 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        Minutes minutes0 = Minutes.TWO;
        Minutes minutes1 = Minutes.THREE;
        Minutes minutes2 = minutes0.minus(minutes1);
        assertEquals((-1), minutes2.getMinutes());
    }
}
