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

public class AtomicDoubleArray_ESTestTest22 extends AtomicDoubleArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        AtomicDoubleArray atomicDoubleArray0 = new AtomicDoubleArray(2924);
        DoubleUnaryOperator doubleUnaryOperator0 = DoubleUnaryOperator.identity();
        // Undeclared exception!
        try {
            atomicDoubleArray0.updateAndGet(2924, doubleUnaryOperator0);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // index 2924
            //
            verifyException("java.util.concurrent.atomic.AtomicLongArray", e);
        }
    }
}
