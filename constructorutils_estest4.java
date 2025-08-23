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

public class ConstructorUtils_ESTestTest4 extends ConstructorUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        Class<Object> class0 = Object.class;
        Class<Annotation>[] classArray0 = (Class<Annotation>[]) Array.newInstance(Class.class, 0);
        Constructor<Object> constructor0 = ConstructorUtils.getAccessibleConstructor(class0, (Class<?>[]) classArray0);
        AccessibleObject[] accessibleObjectArray0 = new AccessibleObject[8];
        accessibleObjectArray0[0] = (AccessibleObject) constructor0;
        accessibleObjectArray0[1] = (AccessibleObject) constructor0;
        accessibleObjectArray0[2] = (AccessibleObject) constructor0;
        accessibleObjectArray0[3] = (AccessibleObject) constructor0;
        accessibleObjectArray0[4] = (AccessibleObject) constructor0;
        accessibleObjectArray0[5] = (AccessibleObject) constructor0;
        accessibleObjectArray0[6] = (AccessibleObject) constructor0;
        accessibleObjectArray0[7] = (AccessibleObject) constructor0;
        AccessibleObject.setAccessible(accessibleObjectArray0, true);
        Constructor<Object> constructor1 = ConstructorUtils.getAccessibleConstructor(constructor0);
        assertSame(constructor0, constructor1);
    }
}
