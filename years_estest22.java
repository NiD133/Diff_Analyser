package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest22 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        Instant instant0 = Instant.EPOCH;
        Years years0 = Years.yearsBetween((ReadableInstant) instant0, (ReadableInstant) instant0);
        assertEquals(0, years0.getYears());
    }
}
