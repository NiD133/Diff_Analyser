package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest3 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Minutes minutes0 = Minutes.TWO;
        Minutes minutes1 = Minutes.THREE;
        boolean boolean0 = minutes0.isGreaterThan(minutes1);
        assertFalse(boolean0);
    }
}
