package com.google.common.reflect;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.annotation.Annotation;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class Parameter_ESTestTest8 extends Parameter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        Parameter parameter0 = null;
        try {
            parameter0 = new Parameter((Invokable<?, ?>) null, 14, (TypeToken<?>) null, (Annotation[]) null, (Object) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.common.collect.ImmutableList", e);
        }
    }
}
