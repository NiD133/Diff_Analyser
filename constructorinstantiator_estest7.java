package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ConstructorInstantiator_ESTestTest7 extends ConstructorInstantiator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test6() throws Throwable {
        Object[] objectArray0 = new Object[0];
        ConstructorInstantiator constructorInstantiator0 = new ConstructorInstantiator(true, objectArray0);
        Class<Object> class0 = Object.class;
        Object object0 = constructorInstantiator0.newInstance(class0);
        assertNotNull(object0);
    }
}
