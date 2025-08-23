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

public class AtomicDoubleArray_ESTestTest19 extends AtomicDoubleArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        AtomicDoubleArray atomicDoubleArray0 = new AtomicDoubleArray(3740);
        DoubleBinaryOperator doubleBinaryOperator0 = mock(DoubleBinaryOperator.class, new ViolatedAssumptionAnswer());
        doReturn((-1.0)).when(doubleBinaryOperator0).applyAsDouble(anyDouble(), anyDouble());
        double double0 = atomicDoubleArray0.accumulateAndGet(67, 3740, doubleBinaryOperator0);
        assertEquals((-1.0), double0, 0.01);
        assertEquals(3740, atomicDoubleArray0.length());
    }
}
