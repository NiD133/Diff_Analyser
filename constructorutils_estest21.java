package org.apache.commons.lang3.reflect;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ConstructorUtils_ESTestTest21 extends ConstructorUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        Class<Object> class0 = Object.class;
        Class<Annotation>[] classArray0 = (Class<Annotation>[]) Array.newInstance(Class.class, 0);
        Constructor<Object> constructor0 = ConstructorUtils.getAccessibleConstructor(class0, (Class<?>[]) classArray0);
        Annotation[] annotationArray0 = constructor0.getAnnotations();
        Class<Annotation> class1 = Annotation.class;
        Class<Object>[] classArray1 = (Class<Object>[]) Array.newInstance(Class.class, 4);
        try {
            ConstructorUtils.invokeConstructor(class1, (Object[]) annotationArray0, (Class<?>[]) classArray1);
            fail("Expecting exception: NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            //
            // No such accessible constructor on object: java.lang.annotation.Annotation
            //
            verifyException("org.apache.commons.lang3.reflect.ConstructorUtils", e);
        }
    }
}
