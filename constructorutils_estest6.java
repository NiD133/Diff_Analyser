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

public class ConstructorUtils_ESTestTest6 extends ConstructorUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        // Undeclared exception!
        try {
            ConstructorUtils.invokeExactConstructor((Class<Annotation>) null, (Object[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // cls
            //
            verifyException("java.util.Objects", e);
        }
    }
}