package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IEEE754rUtils_ESTestTest32 extends IEEE754rUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        double double0 = IEEE754rUtils.max((double) 0.0F, (double) 0.0F);
        assertEquals(0.0, double0, 0.01);
    }
}
