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

public class AtomicDoubleArray_ESTestTest35 extends AtomicDoubleArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test34() throws Throwable {
        AtomicDoubleArray atomicDoubleArray0 = null;
        try {
            atomicDoubleArray0 = new AtomicDoubleArray((double[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.common.util.concurrent.AtomicDoubleArray", e);
        }
    }
}
