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

public class AtomicDoubleArray_ESTestTest42 extends AtomicDoubleArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test41() throws Throwable {
        double[] doubleArray0 = new double[8];
        AtomicDoubleArray atomicDoubleArray0 = new AtomicDoubleArray(doubleArray0);
        DoubleBinaryOperator doubleBinaryOperator0 = mock(DoubleBinaryOperator.class, new ViolatedAssumptionAnswer());
        doReturn((-1.0)).when(doubleBinaryOperator0).applyAsDouble(anyDouble(), anyDouble());
        double double0 = atomicDoubleArray0.getAndAccumulate(0, 1303.997034846174, doubleBinaryOperator0);
        assertEquals(0.0, double0, 0.01);
        boolean boolean0 = atomicDoubleArray0.compareAndSet(0, 0.0, 0.0);
        assertFalse(boolean0);
        assertEquals(8, atomicDoubleArray0.length());
    }
}
