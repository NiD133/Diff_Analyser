package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Arrays;
import org.apache.commons.lang3.function.FailableIntFunction;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

public class ArrayFill_ESTestTest8 extends ArrayFill_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        double[] doubleArray0 = new double[0];
        double[] doubleArray1 = ArrayFill.fill(doubleArray0, (double) (-259));
        assertEquals(0, doubleArray1.length);
    }
}
