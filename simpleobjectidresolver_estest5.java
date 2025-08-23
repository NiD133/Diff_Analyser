package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SimpleObjectIdResolver_ESTestTest5 extends SimpleObjectIdResolver_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test4() throws Throwable {
        SimpleObjectIdResolver simpleObjectIdResolver0 = new SimpleObjectIdResolver();
        Class<Object> class0 = Object.class;
        Class<String> class1 = String.class;
        ObjectIdGenerator.IdKey objectIdGenerator_IdKey0 = new ObjectIdGenerator.IdKey(class0, class1, simpleObjectIdResolver0);
        simpleObjectIdResolver0.bindItem(objectIdGenerator_IdKey0, (Object) null);
        Object object0 = simpleObjectIdResolver0.resolveId(objectIdGenerator_IdKey0);
        assertNull(object0);
    }
}
