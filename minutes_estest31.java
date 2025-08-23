package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest31 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        Minutes minutes0 = Minutes.MIN_VALUE;
        Minutes minutes1 = minutes0.MAX_VALUE.dividedBy((-82));
        minutes1.negated();
        assertEquals((-26188824), minutes1.getMinutes());
    }
}
