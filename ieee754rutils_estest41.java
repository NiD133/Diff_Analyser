package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IEEE754rUtils_ESTestTest41 extends IEEE754rUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        float float0 = IEEE754rUtils.min(0.0F, 0.0F, 0.0F);
        assertEquals(0.0F, float0, 0.01F);
    }
}
