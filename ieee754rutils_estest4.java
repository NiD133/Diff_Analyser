package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IEEE754rUtils_ESTestTest4 extends IEEE754rUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        double[] doubleArray0 = new double[1];
        doubleArray0[0] = (double) 1.0F;
        double double0 = IEEE754rUtils.min(doubleArray0);
        assertEquals(1.0, double0, 0.01);
    }
}
