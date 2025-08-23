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

public class ConstructorUtils_ESTestTest18 extends ConstructorUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        Class<Object> class0 = Object.class;
        Class<Object>[] classArray0 = (Class<Object>[]) Array.newInstance(Class.class, 1);
        Constructor<Object> constructor0 = ConstructorUtils.getMatchingAccessibleConstructor(class0, (Class<?>[]) classArray0);
        assertNull(constructor0);
    }
}