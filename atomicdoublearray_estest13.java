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

public class AtomicDoubleArray_ESTestTest13 extends AtomicDoubleArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        double[] doubleArray0 = new double[9];
        doubleArray0[0] = (-1583.803774);
        AtomicDoubleArray atomicDoubleArray0 = new AtomicDoubleArray(doubleArray0);
        DoubleBinaryOperator doubleBinaryOperator0 = mock(DoubleBinaryOperator.class, new ViolatedAssumptionAnswer());
        doReturn(0.0).when(doubleBinaryOperator0).applyAsDouble(anyDouble(), anyDouble());
        double double0 = atomicDoubleArray0.getAndAccumulate(0, (-1583.803774), doubleBinaryOperator0);
        assertEquals((-1583.803774), double0, 0.01);
    }
}
