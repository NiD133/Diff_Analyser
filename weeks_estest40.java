package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest40 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test39() throws Throwable {
        Weeks weeks0 = Weeks.THREE;
        boolean boolean0 = weeks0.isLessThan((Weeks) null);
        assertFalse(boolean0);
    }
}
