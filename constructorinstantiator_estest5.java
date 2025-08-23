package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ConstructorInstantiator_ESTestTest5 extends ConstructorInstantiator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test4() throws Throwable {
        Object[] objectArray0 = new Object[7];
        ConstructorInstantiator constructorInstantiator0 = new ConstructorInstantiator(true, objectArray0);
        Class<Object> class0 = Object.class;
        // Undeclared exception!
        try {
            constructorInstantiator0.newInstance(class0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
        }
    }
}
