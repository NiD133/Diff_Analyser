package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IEEE754rUtils_ESTestTest44 extends IEEE754rUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test43() throws Throwable {
        float float0 = IEEE754rUtils.max(2162.2F, 0.0F, 2780.809F);
        assertEquals(2780.809F, float0, 0.01F);
    }
}
