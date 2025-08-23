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

public class ConstructorUtils_ESTestTest14 extends ConstructorUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        Class<Object>[] classArray0 = (Class<Object>[]) Array.newInstance(Class.class, 9);
        Class<Integer> class0 = Integer.class;
        try {
            ConstructorUtils.invokeExactConstructor(class0, (Object[]) classArray0, (Class<?>[]) classArray0);
            fail("Expecting exception: NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            //
            // No such accessible constructor on object: java.lang.Integer
            //
            verifyException("org.apache.commons.lang3.reflect.ConstructorUtils", e);
        }
    }
}
