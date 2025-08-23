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

public class AtomicDoubleArray_ESTestTest20 extends AtomicDoubleArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        AtomicDoubleArray atomicDoubleArray0 = new AtomicDoubleArray(93);
        // Undeclared exception!
        try {
            atomicDoubleArray0.weakCompareAndSet(93, 93, 93);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // index 93
            //
            verifyException("java.util.concurrent.atomic.AtomicLongArray", e);
        }
    }
}
