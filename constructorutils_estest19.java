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

public class ConstructorUtils_ESTestTest19 extends ConstructorUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        Class<Object> class0 = Object.class;
        Class<Annotation>[] classArray0 = (Class<Annotation>[]) Array.newInstance(Class.class, 0);
        Constructor<Object> constructor0 = ConstructorUtils.getAccessibleConstructor(class0, (Class<?>[]) classArray0);
        Constructor<Object> constructor1 = ConstructorUtils.getAccessibleConstructor(constructor0);
        assertSame(constructor0, constructor1);
    }
}
