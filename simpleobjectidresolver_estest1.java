package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SimpleObjectIdResolver_ESTestTest1 extends SimpleObjectIdResolver_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
        SimpleObjectIdResolver simpleObjectIdResolver0 = new SimpleObjectIdResolver();
        Class<Object> class0 = Object.class;
        ObjectIdGenerator.IdKey objectIdGenerator_IdKey0 = new ObjectIdGenerator.IdKey(class0, class0, simpleObjectIdResolver0);
        simpleObjectIdResolver0.bindItem(objectIdGenerator_IdKey0, class0);
        Class class1 = (Class) simpleObjectIdResolver0.resolveId(objectIdGenerator_IdKey0);
        assertFalse(class1.isEnum());
    }
}
