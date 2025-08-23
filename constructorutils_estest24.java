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

public class ConstructorUtils_ESTestTest24 extends ConstructorUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        Class<Object> class0 = Object.class;
        Class<Annotation>[] classArray0 = (Class<Annotation>[]) Array.newInstance(Class.class, 0);
        Constructor<Object> constructor0 = ConstructorUtils.getAccessibleConstructor(class0, (Class<?>[]) classArray0);
        Class<?>[] classArray1 = constructor0.getExceptionTypes();
        Object object0 = ConstructorUtils.invokeExactConstructor(class0, (Object[]) classArray1);
        assertNotNull(object0);
    }
}
