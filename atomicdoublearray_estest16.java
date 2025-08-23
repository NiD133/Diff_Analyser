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

public class AtomicDoubleArray_ESTestTest16 extends AtomicDoubleArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        double[] doubleArray0 = new double[6];
        doubleArray0[0] = (-970.960157577528);
        AtomicDoubleArray atomicDoubleArray0 = new AtomicDoubleArray(doubleArray0);
        double double0 = atomicDoubleArray0.addAndGet(0, 0.0);
        assertEquals((-970.960157577528), double0, 0.01);
    }
}
