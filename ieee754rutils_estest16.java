package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IEEE754rUtils_ESTestTest16 extends IEEE754rUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        double[] doubleArray0 = new double[7];
        doubleArray0[0] = (double) (-1742.84F);
        doubleArray0[1] = (-163.0);
        doubleArray0[2] = (double) (-835.94F);
        doubleArray0[3] = (double) (-1.0F);
        doubleArray0[4] = (double) (-835.94F);
        doubleArray0[5] = (double) (-1742.84F);
        doubleArray0[6] = (double) (-1.0F);
        double double0 = IEEE754rUtils.max(doubleArray0);
        assertEquals((-1.0), double0, 0.01);
    }
}
