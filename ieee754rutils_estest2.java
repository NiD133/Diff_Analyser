package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IEEE754rUtils_ESTestTest2 extends IEEE754rUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        float[] floatArray0 = new float[6];
        floatArray0[0] = 1.0F;
        floatArray0[1] = 1.0F;
        floatArray0[2] = 825.0F;
        floatArray0[3] = 1.0F;
        floatArray0[4] = 1.0F;
        floatArray0[5] = 825.0F;
        float float0 = IEEE754rUtils.min(floatArray0);
        assertEquals(1.0F, float0, 0.01F);
    }
}
