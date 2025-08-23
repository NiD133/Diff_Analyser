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

public class ConstructorUtils_ESTestTest10 extends ConstructorUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        Class<Integer>[] classArray0 = (Class<Integer>[]) Array.newInstance(Class.class, 1);
        Class<Object> class0 = Object.class;
        try {
            ConstructorUtils.invokeConstructor(class0, (Object[]) classArray0);
            fail("Expecting exception: NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            //
            // No such accessible constructor on object: java.lang.Object
            //
            verifyException("org.apache.commons.lang3.reflect.ConstructorUtils", e);
        }
    }
}
