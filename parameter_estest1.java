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

public class Parameter_ESTestTest1 extends Parameter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Invokable<Object, Annotation> invokable0 = (Invokable<Object, Annotation>) mock(Invokable.class, new ViolatedAssumptionAnswer());
        Class<Annotation> class0 = Annotation.class;
        TypeToken<Annotation> typeToken0 = TypeToken.of(class0);
        Annotation[] annotationArray0 = new Annotation[0];
        Object object0 = new Object();
        Parameter parameter0 = new Parameter(invokable0, 2473, typeToken0, annotationArray0, object0);
        Parameter parameter1 = new Parameter((Invokable<?, ?>) null, 1021, typeToken0, annotationArray0, parameter0);
        boolean boolean0 = parameter0.equals(parameter1);
        assertFalse(boolean0);
    }
}
