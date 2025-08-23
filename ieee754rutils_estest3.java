package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IEEE754rUtils_ESTestTest3 extends IEEE754rUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        float[] floatArray0 = new float[1];
        floatArray0[0] = (-1.0F);
        float float0 = IEEE754rUtils.min(floatArray0);
        assertEquals((-1.0F), float0, 0.01F);
    }
}
