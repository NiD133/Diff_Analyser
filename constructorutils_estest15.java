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

public class ConstructorUtils_ESTestTest15 extends ConstructorUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        Class<Integer>[] classArray0 = (Class<Integer>[]) Array.newInstance(Class.class, 19);
        Class<Object> class0 = Object.class;
        Class<Integer>[] classArray1 = (Class<Integer>[]) Array.newInstance(Class.class, 0);
        // Undeclared exception!
        try {
            ConstructorUtils.invokeExactConstructor(class0, (Object[]) classArray0, (Class<?>[]) classArray1);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }
}
