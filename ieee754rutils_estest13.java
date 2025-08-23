package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IEEE754rUtils_ESTestTest13 extends IEEE754rUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        float[] floatArray0 = new float[9];
        floatArray0[2] = 981.74023F;
        float float0 = IEEE754rUtils.max(floatArray0);
        assertEquals(981.74023F, float0, 0.01F);
    }
}
