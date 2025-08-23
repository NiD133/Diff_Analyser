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

public class AtomicDoubleArray_ESTestTest37 extends AtomicDoubleArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test36() throws Throwable {
        double[] doubleArray0 = new double[12];
        AtomicDoubleArray atomicDoubleArray0 = new AtomicDoubleArray(doubleArray0);
        DoubleUnaryOperator doubleUnaryOperator0 = DoubleUnaryOperator.identity();
        double double0 = atomicDoubleArray0.updateAndGet(4, doubleUnaryOperator0);
        assertEquals(0.0, double0, 0.01);
    }
}
