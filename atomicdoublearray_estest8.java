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

public class AtomicDoubleArray_ESTestTest8 extends AtomicDoubleArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        double[] doubleArray0 = new double[4];
        doubleArray0[1] = 540.0;
        AtomicDoubleArray atomicDoubleArray0 = new AtomicDoubleArray(doubleArray0);
        double double0 = atomicDoubleArray0.getAndSet(1, 2806.574374631918);
        assertEquals(540.0, double0, 0.01);
    }
}
