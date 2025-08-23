package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest22 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        Minutes minutes0 = Minutes.ONE;
        Minutes minutes1 = minutes0.multipliedBy((-5447));
        assertEquals((-5447), minutes1.getMinutes());
    }
}
