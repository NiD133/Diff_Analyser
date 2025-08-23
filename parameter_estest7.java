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

public class Parameter_ESTestTest7 extends Parameter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Annotation[] annotationArray0 = new Annotation[0];
        Object object0 = new Object();
        Parameter parameter0 = new Parameter((Invokable<?, ?>) null, (-948), (TypeToken<?>) null, annotationArray0, object0);
        // Undeclared exception!
        try {
            parameter0.getAnnotatedType();
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // java.lang.Object cannot be cast to java.lang.reflect.AnnotatedType
            //
            verifyException("com.google.common.reflect.Parameter", e);
        }
    }
}
