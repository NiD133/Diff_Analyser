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

public class Parameter_ESTestTest12 extends Parameter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        Class<Object> class0 = Object.class;
        TypeToken<Object> typeToken0 = TypeToken.of(class0);
        Annotation[] annotationArray0 = new Annotation[0];
        Parameter parameter0 = new Parameter((Invokable<?, ?>) null, 2844, typeToken0, annotationArray0, (Object) null);
        Annotation[] annotationArray1 = parameter0.getDeclaredAnnotations();
        assertEquals(0, annotationArray1.length);
    }
}
