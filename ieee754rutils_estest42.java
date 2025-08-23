package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IEEE754rUtils_ESTestTest42 extends IEEE754rUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test41() throws Throwable {
        double double0 = IEEE754rUtils.min(976.5, 0.0, 0.0);
        assertEquals(0.0, double0, 0.01);
    }
}
