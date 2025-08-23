package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest54 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test53() throws Throwable {
        Years years0 = Years.ZERO;
        DurationFieldType durationFieldType0 = years0.getFieldType();
        assertEquals("years", durationFieldType0.getName());
    }
}
