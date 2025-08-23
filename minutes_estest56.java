package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest56 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test55() throws Throwable {
        Minutes minutes0 = Minutes.minutesIn((ReadableInterval) null);
        Duration duration0 = minutes0.toStandardDuration();
        Minutes minutes1 = duration0.toStandardMinutes();
        assertEquals(0, minutes1.getMinutes());
    }
}
