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

public class Parameter_ESTestTest13 extends Parameter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        Invokable<Object, Annotation> invokable0 = (Invokable<Object, Annotation>) mock(Invokable.class, new ViolatedAssumptionAnswer());
        Class<Annotation> class0 = Annotation.class;
        TypeToken<Annotation> typeToken0 = TypeToken.of(class0);
        Annotation[] annotationArray0 = new Annotation[0];
        Parameter parameter0 = new Parameter(invokable0, 1563, typeToken0, annotationArray0, class0);
        boolean boolean0 = parameter0.equals(parameter0);
        assertTrue(boolean0);
    }
}
