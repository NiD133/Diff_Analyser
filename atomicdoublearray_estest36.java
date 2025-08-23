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

public class AtomicDoubleArray_ESTestTest36 extends AtomicDoubleArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test35() throws Throwable {
        AtomicDoubleArray atomicDoubleArray0 = null;
        try {
            atomicDoubleArray0 = new AtomicDoubleArray((-1));
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.concurrent.atomic.AtomicLongArray", e);
        }
    }
}
