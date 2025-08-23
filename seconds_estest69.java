package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Seconds_ESTestTest69 extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test68() throws Throwable {
        Seconds seconds0 = Seconds.seconds(2);
        seconds0.getFieldType();
        assertEquals(2, seconds0.getSeconds());
    }
}
