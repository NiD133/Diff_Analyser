package com.google.common.util.concurrent;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class AtomicDoubleArray_ESTestTest48 extends AtomicDoubleArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test47() throws Throwable {
        double[] doubleArray0 = new double[9];
        AtomicDoubleArray atomicDoubleArray0 = new AtomicDoubleArray(doubleArray0);
        boolean boolean0 = atomicDoubleArray0.weakCompareAndSet(0, 0, (-1.0));
        assertEquals(9, atomicDoubleArray0.length());
        assertTrue(boolean0);
    }
}
