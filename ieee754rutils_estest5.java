package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IEEE754rUtils_ESTestTest5 extends IEEE754rUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        double[] doubleArray0 = new double[8];
        doubleArray0[1] = (-567.84087);
        double double0 = IEEE754rUtils.min(doubleArray0);
        assertEquals((-567.84087), double0, 0.01);
    }
}
