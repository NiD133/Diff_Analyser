package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class SimpleObjectIdResolver_ESTestTest6 extends SimpleObjectIdResolver_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test5() throws Throwable {
        SimpleObjectIdResolver simpleObjectIdResolver0 = new SimpleObjectIdResolver();
        Class<Object> class0 = Object.class;
        ObjectIdGenerator.IdKey objectIdGenerator_IdKey0 = new ObjectIdGenerator.IdKey(class0, class0, simpleObjectIdResolver0);
        simpleObjectIdResolver0.bindItem(objectIdGenerator_IdKey0, class0);
        // Undeclared exception!
        try {
            simpleObjectIdResolver0.bindItem(objectIdGenerator_IdKey0, (Object) null);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Object Id conflict: Id [ObjectId: key=com.fasterxml.jackson.annotation.SimpleObjectIdResolver@1, type=java.lang.Object, scope=java.lang.Object] already bound to an Object (type: `java.lang.Class`, value: java.lang.Class@0000000003): attempt to re-bind to a different Object (null)
            //
            verifyException("com.fasterxml.jackson.annotation.SimpleObjectIdResolver", e);
        }
    }
}
