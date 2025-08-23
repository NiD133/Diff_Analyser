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

public class AtomicDoubleArray_ESTestTest11 extends AtomicDoubleArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        AtomicDoubleArray atomicDoubleArray0 = new AtomicDoubleArray(3813);
        DoubleBinaryOperator doubleBinaryOperator0 = mock(DoubleBinaryOperator.class, new ViolatedAssumptionAnswer());
        doReturn((-558.4039384)).when(doubleBinaryOperator0).applyAsDouble(anyDouble(), anyDouble());
        double double0 = atomicDoubleArray0.getAndAccumulate(1661, 1.0, doubleBinaryOperator0);
        assertEquals(0.0, double0, 0.01);
        double double1 = atomicDoubleArray0.getAndAdd(1661, 0.0);
        assertEquals((-558.4039384), double1, 0.01);
        assertEquals(3813, atomicDoubleArray0.length());
    }
}
