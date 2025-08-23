package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ConstructorInstantiator_ESTestTest2 extends ConstructorInstantiator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test1() throws Throwable {
        ConstructorInstantiator constructorInstantiator0 = new ConstructorInstantiator(false, (Object[]) null);
        Class<Object> class0 = Object.class;
        // Undeclared exception!
        try {
            constructorInstantiator0.newInstance(class0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.mockito.internal.creation.instance.ConstructorInstantiator", e);
        }
    }
}
