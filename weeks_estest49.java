package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Weeks_ESTestTest49 extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test48() throws Throwable {
        Weeks weeks0 = Weeks.MAX_VALUE;
        Weeks weeks1 = weeks0.plus((Weeks) null);
        assertEquals(1, weeks1.size());
    }
}
