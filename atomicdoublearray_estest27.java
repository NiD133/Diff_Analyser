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

public class AtomicDoubleArray_ESTestTest27 extends AtomicDoubleArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        AtomicDoubleArray atomicDoubleArray0 = new AtomicDoubleArray(1027);
        DoubleUnaryOperator doubleUnaryOperator0 = DoubleUnaryOperator.identity();
        // Undeclared exception!
        try {
            atomicDoubleArray0.getAndUpdate(1027, doubleUnaryOperator0);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // index 1027
            //
            verifyException("java.util.concurrent.atomic.AtomicLongArray", e);
        }
    }
}
