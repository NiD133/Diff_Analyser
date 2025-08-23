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

public class ConstructorUtils_ESTestTest17 extends ConstructorUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        Class<Integer>[] classArray0 = (Class<Integer>[]) Array.newInstance(Class.class, 1);
        Class<Integer> class0 = Integer.class;
        Constructor<Integer> constructor0 = ConstructorUtils.getMatchingAccessibleConstructor(class0, (Class<?>[]) classArray0);
        assertNotNull(constructor0);
        assertEquals("public java.lang.Integer(java.lang.String) throws java.lang.NumberFormatException", constructor0.toString());
    }
}
