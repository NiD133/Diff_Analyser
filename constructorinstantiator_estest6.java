package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ConstructorInstantiator_ESTestTest6 extends ConstructorInstantiator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test5() throws Throwable {
        Object[] objectArray0 = new Object[0];
        ConstructorInstantiator constructorInstantiator0 = new ConstructorInstantiator(true, objectArray0);
        Class<Integer> class0 = Integer.class;
        // Undeclared exception!
        try {
            constructorInstantiator0.newInstance(class0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
        }
    }
}
