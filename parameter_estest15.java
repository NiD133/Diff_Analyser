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

public class Parameter_ESTestTest15 extends Parameter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        Annotation[] annotationArray0 = new Annotation[0];
        Parameter parameter0 = new Parameter((Invokable<?, ?>) null, (-3558), (TypeToken<?>) null, annotationArray0, (Object) null);
        Parameter parameter1 = new Parameter((Invokable<?, ?>) null, 1, (TypeToken<?>) null, annotationArray0, parameter0);
        boolean boolean0 = parameter0.equals(parameter1);
        assertFalse(boolean0);
    }
}
